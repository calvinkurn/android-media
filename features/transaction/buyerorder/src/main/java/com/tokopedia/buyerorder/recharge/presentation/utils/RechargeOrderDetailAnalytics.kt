package com.tokopedia.buyerorder.recharge.presentation.utils

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 03/11/2021
 */
class RechargeOrderDetailAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    fun eventOpenScreen(screenName: String) {
        val map = mapOf(
            Keys.EVENT_NAME to EventName.OPEN_SCREEN,
            Keys.CURRENT_SITE to DefaultValue.CURRENT_SITE,
            Keys.BUSINESS_UNIT to DefaultValue.BUSINESS_UNIT,
            Keys.IS_LOGGED_IN_STATUS to userSession.isLoggedIn.toString(),
            Keys.USER_ID to userSession.userId,
            Keys.SCREEN_NAME to screenName
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSeeInvoice(categoryName: String, operatorName: String) {
        val map = mapOf(
            Keys.EVENT_NAME to EventName.CLICK_CHECKOUT,
            Keys.EVENT_ACTION to EventAction.CLICK_SEE_INVOICE,
            Keys.EVENT_CATEGORY to DefaultValue.EVENT_CATEGORY,
            Keys.EVENT_LABEL to "$categoryName - $operatorName",
            Keys.CURRENT_SITE to DefaultValue.CURRENT_SITE,
            Keys.BUSINESS_UNIT to DefaultValue.BUSINESS_UNIT
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickCopyButton(categoryName: String, operatorName: String) {
        val map = mapOf(
            Keys.EVENT_NAME to EventName.CLICK_CHECKOUT,
            Keys.EVENT_ACTION to EventAction.CLICK_COPY_BUTTON,
            Keys.EVENT_CATEGORY to DefaultValue.EVENT_CATEGORY,
            Keys.EVENT_LABEL to "$categoryName - $operatorName",
            Keys.CURRENT_SITE to DefaultValue.CURRENT_SITE,
            Keys.BUSINESS_UNIT to DefaultValue.BUSINESS_UNIT
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventTopAdsImpression(data: RecommendationItem) {
        val bundle = Bundle().apply {
            putString(Keys.EVENT_NAME, EventName.VIEW_ITEM_LIST)
            putString(Keys.EVENT_ACTION, EventAction.IMPRESSION_PRODUCT)
            putString(Keys.EVENT_CATEGORY, DefaultValue.TOPADS_EVENT_CATEGORY)
            putString(Keys.EVENT_LABEL, "")
            putString(Keys.BUSINESS_UNIT, DefaultValue.BUSINESS_UNIT)
            putString(Keys.CURRENT_SITE, DefaultValue.CURRENT_SITE)
            putParcelableArrayList(Keys.ITEMS, arrayListOf(mapTopAdsProduct(data)))
            putString(Keys.USER_ID, userSession.userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            EventName.VIEW_ITEM_LIST,
            bundle
        )
    }

    fun eventTopAdsClick(data: RecommendationItem) {
        val bundle = Bundle().apply {
            putString(Keys.EVENT_NAME, EventName.SELECT_CONTENT)
            putString(Keys.EVENT_ACTION, EventAction.CLICK_PRODUCT)
            putString(Keys.EVENT_CATEGORY, DefaultValue.TOPADS_EVENT_CATEGORY)
            putString(Keys.EVENT_LABEL, "")
            putString(Keys.BUSINESS_UNIT, DefaultValue.BUSINESS_UNIT)
            putString(Keys.CURRENT_SITE, DefaultValue.CURRENT_SITE)
            putString(Keys.ITEM_LIST, "")
            putParcelableArrayList(Keys.ITEMS, arrayListOf(mapTopAdsProduct(data)))
            putString(Keys.USER_ID, userSession.userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            EventName.SELECT_CONTENT,
            bundle
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/2775
    // Tracker ID: 28223
    fun sendViewVoidPopupEvent(
        categoryName: String,
        productId: String,
        orderStatus: String
    ) {
        val eventLabel = "$categoryName - $productId - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.VIEW_DIGITAL_IRIS)
            .setEventAction(EventAction.VIEW_VOID_POPUP)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.VIEW_VOID_POPUP)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/2775
    // Tracker ID: 28227
    fun sendClickKembaliVoidPopupEvent(
        categoryName: String,
        productId: String,
        orderStatus: String
    ) {
        val eventLabel = "$categoryName - $productId - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.CLICK_DIGITAL)
            .setEventAction(EventAction.CLICK_KEMBALI_VOID_POPUP)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.CLICK_KEMBALI_VOID_POPUP)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/2775
    // Tracker ID: 28224
    fun sendClickBatalkanVoidPopupEvent(
        categoryName: String,
        productId: String,
        orderStatus: String
    ) {
        val eventLabel = "$categoryName - $productId - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.CLICK_DIGITAL)
            .setEventAction(EventAction.CLICK_BATALKAN_VOID_POPUP)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.CLICK_BATALKAN_VOID_POPUP)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/272
    // Tracker ID: 50618
    fun sendImpressionPrimaryButtonEvent(
        categoryName: String,
        productId: String,
        orderStatus: String,
        buttonName: String
    ) {
        val eventLabel = "$categoryName - $productId - $buttonName - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.VIEW_DIGITAL_IRIS)
            .setEventAction(EventAction.IMPRESSION_PRIMARY_BUTTON)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.IMPRESSION_PRIMARY_BUTTON)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/272
    // Tracker ID: 50619
    fun sendImpressionSecondaryButtonEvent(
        categoryName: String,
        productId: String,
        orderStatus: String,
        buttonName: String
    ) {
        val eventLabel = "$categoryName - $productId - $buttonName - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.VIEW_DIGITAL_IRIS)
            .setEventAction(EventAction.IMPRESSION_SECONDARY_BUTTON)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.IMPRESSION_SECONDARY_BUTTON)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/272
    // Tracker ID: 1559
    fun sendClickPrimaryButtonEvent(
        categoryName: String,
        productId: String,
        orderStatus: String,
        buttonName: String
    ) {
        val eventLabel = "$categoryName - $productId - $buttonName - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.CLICK_DIGITAL)
            .setEventAction(EventAction.CLICK_PRIMARY_BUTTON)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.CLICK_PRIMARY_BUTTON)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/272
    // Tracker ID: 1560
    fun sendClickSecondaryButtonEvent(
        categoryName: String,
        productId: String,
        orderStatus: String,
        buttonName: String
    ) {
        val eventLabel = "$categoryName - $productId - $buttonName - $orderStatus"
        Tracker.Builder()
            .setEvent(EventName.CLICK_DIGITAL)
            .setEventAction(EventAction.CLICK_SECONDARY_BUTTON)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.CLICK_SECONDARY_BUTTON)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/272
    // Tracker ID: 19558
    fun sendClickOnFeatureButtonEvent(
        categoryName: String,
        productId: String,
        buttonName: String
    ) {
        val eventLabel = "$categoryName - $productId - $buttonName"
        Tracker.Builder()
            .setEvent(EventName.CLICK_CHECKOUT)
            .setEventAction(EventAction.CLICK_FEATURE_BUTTON)
            .setEventCategory(DefaultValue.EVENT_CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(Keys.TRACKER_ID, TrackerId.CLICK_FEATURE_BUTTON)
            .setBusinessUnit(DefaultValue.BUSINESS_UNIT)
            .setCurrentSite(DefaultValue.CURRENT_SITE)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun mapTopAdsProduct(data: RecommendationItem): Bundle =
        Bundle().apply {
            putString(Keys.INDEX, data.position.toString())
            putString(Keys.ITEM_BRAND, "")
            putString(Keys.ITEM_CATEGORY, data.categoryBreadcrumbs)
            putString(Keys.ITEM_ID, data.productId.toString())
            putString(Keys.ITEM_NAME, data.name)
            putString(Keys.ITEM_VARIANT, "")
            putString(Keys.PRICE, data.priceInt.toString())
        }

    class Keys {
        companion object {
            const val EVENT_NAME = "event"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_LABEL = "eventLabel"

            const val SCREEN_NAME = "screenName"
            const val CURRENT_SITE = "currentSite"
            const val BUSINESS_UNIT = "businessUnit"
            const val CATEGORY = "category"
            const val USER_ID = "userId"
            const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"

            const val ITEM_LIST = "item_list"

            const val ITEMS = "items"
            const val INDEX = "index"
            const val ITEM_BRAND = "item_brand"
            const val ITEM_CATEGORY = "item_category"
            const val ITEM_ID = "item_id"
            const val ITEM_NAME = "item_name"
            const val ITEM_VARIANT = "item_variant"
            const val PRICE = "price"
            const val TRACKER_ID = "trackerId"
        }
    }

    class DefaultValue {
        companion object {
            const val SCREEN_NAME_ORDER_DETAIL = "/order-detail-digital"
            const val SCREEN_NAME_INVOICE = "/invoice-page-digital"

            const val BUSINESS_UNIT = "recharge"
            const val CURRENT_SITE = "tokopediadigital"

            const val EVENT_CATEGORY = "digital - order detail page"
            const val TOPADS_EVENT_CATEGORY = "topads DG order detail"
        }
    }

    class EventAction {
        companion object {
            const val CLICK_SEE_INVOICE = "click lihat invoice"
            const val CLICK_COPY_BUTTON = "click copy button"
            const val CLICK_FEATURE_BUTTON = "click on feature button"

            const val IMPRESSION_PRODUCT = "impression product"
            const val CLICK_PRODUCT = "click product"

            const val VIEW_VOID_POPUP = "view void popup"
            const val CLICK_BATALKAN_VOID_POPUP = "click batalkan void popup"
            const val CLICK_KEMBALI_VOID_POPUP = "click kembali void popup"

            const val CLICK_PRIMARY_BUTTON = "click primary button"
            const val CLICK_SECONDARY_BUTTON = "click secondary button"
            const val IMPRESSION_PRIMARY_BUTTON = "impression primary button"
            const val IMPRESSION_SECONDARY_BUTTON = "impression secondary button"
        }
    }

    class EventName {
        companion object {
            const val OPEN_SCREEN = "openScreen"
            const val CLICK_CHECKOUT = "clickCheckout"
            const val CLICK_DIGITAL = "clickDigital"
            const val VIEW_ITEM_LIST = "view_item_list"
            const val SELECT_CONTENT = "select_content"
            const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
        }
    }

    class TrackerId {
        companion object {
            const val VIEW_VOID_POPUP = "28223"
            const val CLICK_BATALKAN_VOID_POPUP = "28224"
            const val CLICK_KEMBALI_VOID_POPUP = "28227"
            const val CLICK_PRIMARY_BUTTON = "1559"
            const val CLICK_SECONDARY_BUTTON = "1560"
            const val CLICK_FEATURE_BUTTON = "19558"
            const val IMPRESSION_PRIMARY_BUTTON = "50618"
            const val IMPRESSION_SECONDARY_BUTTON = "50619"
        }
    }
}
