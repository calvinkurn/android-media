package com.tokopedia.recommendation_widget_common.domain.coroutines

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.byteio.RecommendationByteIoUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQuery
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 */
open class GetRecommendationUseCase @Inject constructor(
    private val context: Context,
    graphqlRepository: GraphqlRepository,
) : UseCase<GetRecommendationRequestParam, List<RecommendationWidget>>() {

    private val graphqlUseCase = GraphqlUseCase<RecommendationEntity>(graphqlRepository)
    private val byteIoUseCase = RecommendationByteIoUseCase()

    init {
        graphqlUseCase.setTypeClass(RecommendationEntity::class.java)
        graphqlUseCase.setGraphqlQuery(ListProductRecommendationQuery())
    }

    override suspend fun getData(inputParameter: GetRecommendationRequestParam): List<RecommendationWidget> {
        val userSession = UserSession(context)
        inputParameter.userId = userSession.userId.toIntOrNull() ?: 0

        val queryParam = ChooseAddressUtils
            .getLocalizingAddressData(context)
            .toQueryParam(inputParameter.queryParam)

        val parameter = byteIoUseCase.getParameter(inputParameter).copy(queryParam = queryParam).toGqlRequest()

        graphqlUseCase.setRequestParams(parameter)

        return graphqlUseCase.executeOnBackground()
            .productRecommendationWidget.data
            .mappingToRecommendationModel(inputParameter.totalData)
            .also {
                byteIoUseCase.updateSessionId(
                    inputParameter.pageName,
                    it.firstOrNull()?.appLog?.sessionId.orEmpty()
                )
            }
    }
}
