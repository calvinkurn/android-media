package com.tokopedia.thankyou_native.analytics

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.BUSINESS_UNIT_TOKOPOINTS
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLICK_GYRO_RECOM
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLICK_TOKOMEMBER_ACION_CLOSE
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLICK_TOKOMEMBER_ACION_OPEN
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLOSE_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_BUSINESS_UNIT
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_EVENT
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_EVENT_ACTION
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_EVENT_CATEGORY
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_EVENT_LABEL
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_PAYMENT_ID
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_PROFILE_ID
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.KEY_USER_ID
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.OPEN_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_CLICK
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_CLICK_TOKOMEMBER
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_CATEGORY
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_CREATIVE
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_CREATIVE_URL
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_ID
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_NAME
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_POSITION
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_KEY_PROMOTIONS
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_VIEW
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.PROMO_VIEW_TOKOMEMBER
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VALUE_ORDER_COMPLETE
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VIEW_GYRO_RECOM
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VIEW_TOKOMEMBER_ACION_CLOSE
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VIEW_TOKOMEMBER_ACION_OPEN
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VIEW_TOKOMEMBER_ACION_OPEN_REGISTERED
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.VIEW_TOKOMEMBER_CATEGORY
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GyroRecommendationAnalytics @Inject constructor(
        val userSession: dagger.Lazy<UserSessionInterface>,
        @CoroutineMainDispatcher val mainDispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val bgDispatcher: CoroutineDispatcher
) {


    private val analyticTracker: ContextAnalytics by lazy(LazyThreadSafetyMode.NONE) {
        TrackApp.getInstance().gtm
    }


    fun onGyroRecommendationListView(gyroRecommendationListItem: GyroRecommendationListItem,
                                     thanksPageData: ThanksPageData, position: Int) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                val data = getParentTrackingNode(PROMO_VIEW, VIEW_GYRO_RECOM,
                        thanksPageData, gyroRecommendationListItem)
                data[ParentTrackingKey.KEY_ECOMMERCE] = getEnhancedECommerceNode(PROMO_VIEW,
                        gyroRecommendationListItem, position)
                analyticTracker.sendEnhanceEcommerceEvent(data)
            }
        }, onError = {})
    }

    fun onGyroRecommendationTokomemberView(gyroRecommendationItem: GyroTokomemberItem,
                                     thanksPageData: ThanksPageData, position: Int) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                val data = getParentTrackingNodeTokoMember(
                    PROMO_VIEW_TOKOMEMBER, getEventActionView(gyroRecommendationItem.membershipType , gyroRecommendationItem.successRegister),
                    thanksPageData, gyroRecommendationItem)
                analyticTracker.sendGeneralEvent(data)
            }
        }, onError = {})
    }

    fun onGyroRecommendationListClick(gyroRecommendationListItem: GyroRecommendationListItem,
                                      thanksPageData: ThanksPageData, position: Int) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                val data = getParentTrackingNode(PROMO_CLICK, CLICK_GYRO_RECOM,
                        thanksPageData, gyroRecommendationListItem)
                data[ParentTrackingKey.KEY_ECOMMERCE] = getEnhancedECommerceNode(PROMO_CLICK,
                        gyroRecommendationListItem, position)
                analyticTracker.sendEnhanceEcommerceEvent(data)
            }
        }, onError = {})
    }

    fun onGyroRecommendationTokomemberClick(gyroRecommendationItem: GyroTokomemberItem,
                                      thanksPageData: ThanksPageData, position: Int) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                val data = getParentTrackingNodeTokoMember(
                    PROMO_CLICK_TOKOMEMBER, getEventActionClick(gyroRecommendationItem.membershipType),
                    thanksPageData, gyroRecommendationItem)
                analyticTracker.sendGeneralEvent(data)
            }
        }, onError = {})
    }

    private fun getEnhancedECommerceNode(key: String, item: GyroRecommendationListItem,
                                         position: Int): Map<String, Any> {
        val promotionsMap = mapOf(PROMO_KEY_ID to "${item.id}",
                PROMO_KEY_NAME to item.title,
                PROMO_KEY_CREATIVE to item.title,
                PROMO_KEY_CREATIVE_URL to item.image,
                PROMO_KEY_POSITION to position,
                PROMO_KEY_CATEGORY to ""
        )
        return mapOf(key to mapOf(PROMO_KEY_PROMOTIONS to arrayListOf(promotionsMap)))
    }



    private fun getParentTrackingNode(eventName: String, eventAction: String,
                                      thanksPageData: ThanksPageData,
                                      gyroRecommendationListItem: GyroRecommendationListItem): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to VALUE_ORDER_COMPLETE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to "${gyroRecommendationListItem.id}-${gyroRecommendationListItem.title}",
                KEY_USER_ID to userSession.get().userId.toString(),
                KEY_PAYMENT_ID to thanksPageData.paymentID,
                KEY_PROFILE_ID to thanksPageData.profileCode
        )
    }

    private fun getParentTrackingNodeTokoMember(eventName: String, eventAction: String,
                                      thanksPageData: ThanksPageData,
                                      gyroRecommendationListItem: GyroTokomemberItem): MutableMap<String, Any> {
        return mutableMapOf(
            KEY_EVENT to eventName,
            KEY_EVENT_CATEGORY to VIEW_TOKOMEMBER_CATEGORY,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to thanksPageData.paymentID,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT_TOKOPOINTS,

        )
    }

    private fun getEventActionView(membershipType: Int, successRegister: Boolean): String {
        return if (successRegister) {
            VIEW_TOKOMEMBER_ACION_OPEN_REGISTERED
        } else {
            when (membershipType) {
                OPEN_MEMBERSHIP -> VIEW_TOKOMEMBER_ACION_OPEN
                CLOSE_MEMBERSHIP -> VIEW_TOKOMEMBER_ACION_CLOSE
                else -> {
                    ""
                }
            }
        }
    }

    private fun getEventActionClick(membershipType: Int): String {
        return when (membershipType) {
            OPEN_MEMBERSHIP -> CLICK_TOKOMEMBER_ACION_OPEN
            CLOSE_MEMBERSHIP -> CLICK_TOKOMEMBER_ACION_CLOSE
            else -> {
                ""
            }
        }
    }
}

object GyroTrackingKeys {
    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_USER_ID = "userId"
    val KEY_PAYMENT_ID = "paymentId"
    val KEY_PROFILE_ID = "profileId"
    val KEY_BUSINESS_UNIT = "businessUnit"

    val VALUE_ORDER_COMPLETE = "order complete"
    val PROMO_CLICK = "promoClick"
    val PROMO_VIEW = "promoView"
    val PROMO_VIEW_TOKOMEMBER = "viewBGPIris"
    val PROMO_CLICK_TOKOMEMBER = "clickBGP"
    val VIEW_GYRO_RECOM = "view gyro recommendation"
    val CLICK_GYRO_RECOM = "click gyro recommendation"
    val VIEW_TOKOMEMBER_ACION_OPEN = "open membership - view widget"
    val VIEW_TOKOMEMBER_ACION_OPEN_REGISTERED = "open membership - view widget after join"
    val CLICK_TOKOMEMBER_ACION_OPEN = "open membership - click widget"
    val VIEW_TOKOMEMBER_ACION_CLOSE = "close membership - view widget"
    val CLICK_TOKOMEMBER_ACION_CLOSE = "close membership - click widget"
    val VIEW_TOKOMEMBER_CATEGORY = "tokomember - thank you page"
    val BUSINESS_UNIT_TOKOPOINTS = "tokopoints"

    const val OPEN_MEMBERSHIP = 1
    const val CLOSE_MEMBERSHIP = 2

    val PROMO_KEY_ID = "id"
    val PROMO_KEY_PROMOTIONS = "promotions"
    val PROMO_KEY_NAME = "name"
    val PROMO_KEY_CREATIVE = "creative"
    val PROMO_KEY_CREATIVE_URL = "creative_url"
    val PROMO_KEY_POSITION = "position"
    val PROMO_KEY_CATEGORY = "category"

}
