package com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom

import android.content.Context
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface

class FakeGetRecommendationUseCase(
    context: Context,
    recomRawString: String,
    private val graphqlUseCase: FakeRecommendationGraphqlUseCase,
    userSession: UserSessionInterface
) : GetRecommendationUseCase(
    context, recomRawString, graphqlUseCase, userSession
) {

    var response = RecommendationEntity()
        set(value) {
            field = value
            graphqlUseCase.response = value
        }

    init {
        response = response
    }

}