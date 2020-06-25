package com.tokopedia.common_digital.common

import com.tokopedia.common_digital.common.presentation.model.RechargePushEventRecommendationResponseEntity
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.track.TrackApp
import rx.Subscriber
import java.util.*

class RechargeAnalytics(private val rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase) {

    fun eventOpenScreen(isLoginStatus: Boolean, categoryName: String, categoryId: String) {
        val stringScreenName = StringBuilder(RECHARGE_SCREEN_NAME)
        stringScreenName.append(categoryName.toLowerCase())

        val mapOpenScreen = HashMap<String, String>()
        mapOpenScreen[EVENT_NAME] = OPEN_SCREEN_EVENT
        mapOpenScreen[IS_LOGIN_STATUS] = if (isLoginStatus) "true" else "false"
        mapOpenScreen[CATEGORY] = categoryName
        mapOpenScreen[CATEGORY_ID] = categoryId
        mapOpenScreen[BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE

        TrackApp.getInstance().gtm.sendScreenAuthenticated(stringScreenName.toString(), mapOpenScreen)
    }

    fun trackVisitRechargePushEventRecommendation(categoryId: Int) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_VISIT),
                getDefaultRechargePushEventRecommendationSubsriber())
    }

    fun trackVisitRechargePushEventRecommendation(categoryId: Int, subscriber: Subscriber<GraphqlResponse>) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_VISIT),
                subscriber)
    }

    fun trackAddToCartRechargePushEventRecommendation(categoryId: Int) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_ATC),
                getDefaultRechargePushEventRecommendationSubsriber())
    }

    fun trackAddToCartRechargePushEventRecommendation(categoryId: Int, subscriber: Subscriber<GraphqlResponse>) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_ATC),
                subscriber)
    }

    private fun getDefaultRechargePushEventRecommendationSubsriber(): Subscriber<GraphqlResponse> {
        return object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val response = graphqlResponse.getData<RechargePushEventRecommendationResponseEntity>(RechargePushEventRecommendationResponseEntity::class.java)
            }
        }
    }

    companion object {
        const val ACTION_VISIT = "VISIT"
        const val ACTION_ATC = "ATC"

        const val DIGITAL_HOMEPAGE = "digital - homepage"

        const val EVENT_NAME = "eventName"
        const val OPEN_SCREEN_EVENT = "openScreen"
        const val IS_LOGIN_STATUS = "isLoggedInStatus"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "digitalCategoryId"
        const val BUSINESS_UNIT = "businessUnit"

        const val RECHARGE_SCREEN_NAME = "/digital/"
        const val BUSINESS_UNIT_RECHARGE = "recharge"
    }
}