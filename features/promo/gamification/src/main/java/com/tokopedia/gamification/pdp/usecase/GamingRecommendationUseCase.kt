package com.tokopedia.gamification.pdp.usecase

import com.tokopedia.gamification.pdp.di.modules.GqlQueryModule
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Named

class GamingRecommendationUseCase constructor(
        @Named(GqlQueryModule.GAMING_RECOMMENDATION_PARAM_QUERY)
        val recomRawString: String,
        val graphqlUseCase: GraphqlUseCase,
        val userSession: UserSessionInterface) : UseCase<List<RecommendationWidget>>() {


    override fun createObservable(requestParams: RequestParams): Observable<List<RecommendationWidget>> {
//        return Observable.just(emptyList())

        val graphqlRequest = GraphqlRequest(recomRawString, RecomendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map<List<RecomendationEntity.RecomendationData>> { graphqlResponse ->
                    val entity = graphqlResponse.getData<RecomendationEntity>(RecomendationEntity::class.java)
                    entity?.productRecommendationWidget?.data
                }
                .map(RecommendationEntityMapper())
    }

}
