package com.tokopedia.common_digital.common

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common_digital.common.presentation.model.RechargePushEventRecommendationResponseEntity
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.track.TrackApp
import rx.Subscriber
import java.lang.StringBuilder
import java.util.HashMap

class RechargeAnalytics(private val rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase) {

    fun eventDigitalCategoryScreenLaunch(categoryName: String, categoryId: String) {
        val value = DataLayer.mapOf(
                PARAM_CATEGORY_NAME, categoryName,
                PARAM_CATEGORY_ID, categoryId
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, EVENT_DIGITAL_CATEGORY_SCREEN_LAUNCH)
    }

    fun eventOpenScreen(isLoginStatus: Boolean, categoryName: String, categoryId: String) {
        val stringScreenName = StringBuilder(RECHARGE_SCREEN_NAME)
        stringScreenName.append(categoryName)

        val mapOpenScreen = HashMap<String, String>()
        mapOpenScreen[EVENT_NAME] = OPEN_SCREEN_EVENT
        mapOpenScreen[IS_LOGIN_STATUS] = if (isLoginStatus) "true" else "false"
        mapOpenScreen[CATEGORY] = categoryName
        mapOpenScreen[CATEGORY_ID] = categoryId

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

        const val PARAM_CATEGORY_NAME = "category"
        const val PARAM_CATEGORY_ID = "digital_category_id"
        const val EVENT_DIGITAL_CATEGORY_SCREEN_LAUNCH = "Digital_Category_Screen_Launched"

        const val CLICK_PDP = "clickPDP"
        const val DIGITAL_HOMEPAGE = "digital - homepage"
        const val CLICK_UPDATE_SALDO = "click update saldo "

        const val EVENT_NAME = "eventName"
        const val OPEN_SCREEN_EVENT = "openScreen"
        const val SCREEN_NAME = "screenName"
        const val IS_LOGIN_STATUS = "isLoggedInStatus"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "digitalCategoryId"

        const val RECHARGE_SCREEN_NAME = "/digital/"


    }
}