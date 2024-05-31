package com.tokopedia.recommendation_widget_common.domain.coroutines

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.byteio.RecommendationByteIoUseCase
import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.query.ProductRecommendationSingleQuery
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.toRecommendationWidget
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 */

open class GetSingleRecommendationUseCase @Inject constructor(
    private val context: Context,
    private val graphqlRepository: GraphqlRepository,
) : UseCase<GetRecommendationRequestParam, RecommendationWidget>() {

    private val byteIoUseCase = RecommendationByteIoUseCase()

    override suspend fun getData(inputParameter: GetRecommendationRequestParam): RecommendationWidget {
        val graphqlUseCase: GraphqlUseCase<SingleProductRecommendationEntity> = GraphqlUseCase(graphqlRepository)


        val queryParam = ChooseAddressUtils
            .getLocalizingAddressData(context)
            .toQueryParam(inputParameter.queryParam)

        val parameter = byteIoUseCase.getParameter(inputParameter).copy(queryParam = queryParam).toGqlRequest()

        graphqlUseCase.setTypeClass(SingleProductRecommendationEntity::class.java)
        graphqlUseCase.setRequestParams(parameter)
        graphqlUseCase.setGraphqlQuery(ProductRecommendationSingleQuery())

        return graphqlUseCase.executeOnBackground().productRecommendationWidget.data.toRecommendationWidget(byteIoUseCase.getTotalData(inputParameter.pageName))
            .also {
                // if the request does not have pageName, use productId as identifier
                val requestIdentifier = inputParameter.pageName.ifEmpty { inputParameter.productIds.joinToString(",") }
                byteIoUseCase.updateMap(
                    requestIdentifier,
                    sessionId = it.appLog.sessionId,
                    totalData = it.recommendationItemList.size
                )
            }
    }
}
