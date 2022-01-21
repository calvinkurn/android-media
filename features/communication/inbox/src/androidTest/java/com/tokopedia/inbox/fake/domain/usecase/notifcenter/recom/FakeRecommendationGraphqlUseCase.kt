package com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.lang.reflect.Type

class FakeRecommendationGraphqlUseCase: GraphqlUseCase() {

    var response: RecommendationEntity = RecommendationEntity()

    override fun createObservable(
        params: RequestParams?
    ): Observable<GraphqlResponse> {
        val map = mutableMapOf<Type, Any>(
            RecommendationEntity::class.java to response
        )
        return Observable.just(GraphqlResponse(
            map, mutableMapOf(), false
        ))
    }
}