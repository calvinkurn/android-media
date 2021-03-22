package com.tokopedia.recommendation_widget_common.domain.coroutines

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 */

open class GetRecommendationUseCase @Inject
constructor(private val context: Context, private val graphqlRepository: GraphqlRepository)
    : UseCase<GetRecommendationRequestParam, List<RecommendationWidget>>() {
    private val graphqlUseCase = GraphqlUseCase<RecommendationEntity>(graphqlRepository)
    init {
        graphqlUseCase.setTypeClass(RecommendationEntity::class.java)
        graphqlUseCase.setGraphqlQuery(GetRecommendationUseCaseRequest.widgetListQuery)
    }
    override suspend fun getData(inputParameter: GetRecommendationRequestParam): List<RecommendationWidget> {
        val queryParam = ChooseAddressUtils.getLocalizingAddressData(context)?.toQueryParam(inputParameter.queryParam) ?: inputParameter.queryParam
        graphqlUseCase.setRequestParams(inputParameter.copy(queryParam = queryParam).toGqlRequest())
        return graphqlUseCase.executeOnBackground().productRecommendationWidget.data.mappingToRecommendationModel()
    }
}
