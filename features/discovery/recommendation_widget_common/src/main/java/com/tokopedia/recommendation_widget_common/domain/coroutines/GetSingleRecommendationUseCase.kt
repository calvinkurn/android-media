package com.tokopedia.recommendation_widget_common.domain.coroutines

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.SingleProductRecommendationMapper
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

import rx.Observable

/**
 * Created by devara fikry on 16/04/19.
 */

open class GetSingleRecommendationUseCase @Inject
constructor(private val graphqlRepository: GraphqlRepository)
    : UseCase<GetRecommendationRequestParam, RecommendationWidget>() {
    override suspend fun getData(inputParameter: GetRecommendationRequestParam): RecommendationWidget {
        val graphqlUseCase: GraphqlUseCase<SingleProductRecommendationEntity> = GraphqlUseCase(graphqlRepository)

        graphqlUseCase.setTypeClass(SingleProductRecommendationEntity::class.java)
        graphqlUseCase.setRequestParams(inputParameter.toGqlRequest())
        graphqlUseCase.setGraphqlQuery(GetRecommendationUseCaseRequest.singleQuery)
        return SingleProductRecommendationMapper.convertIntoRecommendationWidget(
            graphqlUseCase.executeOnBackground().productRecommendationWidget?.data
        )
    }
}
