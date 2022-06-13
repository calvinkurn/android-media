package com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom

import android.content.Context
import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.test.R
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

    val defaultResponse: RecommendationEntity
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_recom, RecommendationEntity::class.java
        )

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }

}