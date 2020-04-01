package com.tokopedia.recommendation_widget_common.domain.coroutines

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

import javax.inject.Inject

import rx.Observable

/**
 * Created by devara fikry on 16/04/19.
 */

open class GetRecommendationUseCase @Inject
constructor(private val graphqlRepository: GraphqlRepository)
    : UseCase<GetRecommendationRequestParam, List<RecommendationWidget>>() {

    override suspend fun getData(inputParameter: GetRecommendationRequestParam): List<RecommendationWidget> {
        val graphqlUseCase = GraphqlUseCase<RecomendationEntity>(graphqlRepository)

        graphqlUseCase.setTypeClass(RecomendationEntity::class.java)
        graphqlUseCase.setRequestParams(inputParameter.toGqlRequest())
        graphqlUseCase.setGraphqlQuery(GetRecommendationUseCaseRequest.widgetListQuery)
        return RecommendationEntityMapper.mappingToRecommendationModel(
                graphqlUseCase.executeOnBackground().productRecommendationWidget?.data?: listOf()
        )
    }
}
