package com.tokopedia.digital.home.analytics

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.CATEGORY
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.ID
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.NAME
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.POSITION
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.PROMO_CODE
import com.tokopedia.digital.home.analytics.DigitaHomepageTrackingEEConstant.PROMO_ID
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.ALL_BANNERS_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.BACK_BUTTON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.DYNAMIC_ICON_IMPRESSION
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.MORE_INFO_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_BOX_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_IMPRESSION
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SUBHOME_WIDGET_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SUBHOME_WIDGET_IMPRESSION
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingCategoryConstant.DIGITAL_HOMEPAGE_CATEGORY
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.CLICK_TOPUP_BILLS
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.FAVOURITE_NUMBER
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.HELP
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.LANGGANAN
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.ORDER_LIST
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class RechargeHomepageAnalytics {

    fun eventBannerImpression(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                ID, item?.id ?: "",
                NAME, item?.title ?: "",
                CREATIVE, item?.title ?: "",
                CREATIVE_URL, item?.filename ?: "",
                POSITION, position,
                CATEGORY, "",
                PROMO_ID, "",
                PROMO_CODE, ""
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventBannerClick(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                ID, item?.id ?: "",
                NAME, item?.title ?: "",
                CREATIVE, item?.title ?: "",
                CREATIVE_URL, item?.filename ?: "",
                POSITION, position,
                CATEGORY, "",
                PROMO_ID, "",
                PROMO_CODE, ""
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventCategoryImpression(items: List<DigitalHomePageCategoryModel.Submenu?>) {
        val products = mutableListOf<Any>()
        for ((index, item) in items.withIndex()) {
            products.add(DataLayer.mapOf(
                    ID, item?.id ?: "",
                    NAME, item?.name ?: "",
                    CREATIVE, item?.name ?: "",
                    CREATIVE_URL, item?.icon ?: "",
                    POSITION, index,
                    CATEGORY, "",
                    PROMO_ID, "",
                    PROMO_CODE, ""
            ))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventCategoryClick(item: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                ID, item?.id ?: "",
                NAME, item?.name ?: "",
                CREATIVE, item?.name ?: "",
                CREATIVE_URL, item?.icon ?: "",
                POSITION, position,
                CATEGORY, "",
                PROMO_ID, "",
                PROMO_CODE, ""
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.name}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventClickOrderList() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, ORDER_LIST)
    }

    fun eventClickLangganan() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, LANGGANAN)
    }

    fun eventClickHelp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, HELP)
    }

    fun eventClickFavNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, FAVOURITE_NUMBER)
    }

    fun eventClickBackButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, BACK_BUTTON_CLICK, "")
    }

    fun eventClickSearchBox() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_BOX_CLICK, "")
    }

    fun eventClickSearch(searchQuery: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_CLICK, searchQuery)
    }

    fun eventClickAllBanners() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, ALL_BANNERS_CLICK, "")
    }

    fun eventClickSubscriptionGuide() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SUBSCRIPTION_GUIDE_CLICK, "")
    }

    fun eventClickMoreInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, MORE_INFO_CLICK, "")
    }

    fun eventSectionImpression(data: List<DigitalHomePageSectionModel.Item>, eventAction: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, eventAction,
                        TrackAppUtils.EVENT_LABEL, "",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, createSectionItem(data).toArray()))
                ))
    }

    fun eventSectionClick(data: DigitalHomePageSectionModel.Item, position: Int, eventAction: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, eventAction,
                        TrackAppUtils.EVENT_LABEL, data.title,
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, createSectionItem(listOf(data), position).toArray()))
                ))

    }

    private fun createSectionItem(list: List<DigitalHomePageSectionModel.Item>, position: Int? = null): ArrayList<Any> {
        val items = ArrayList<Any>()
        for ((index, item) in list.withIndex()) {
            items.add(DataLayer.mapOf(
                    ID, item.id,
                    NAME, item.title,
                    CREATIVE, item.title,
                    CREATIVE_URL, item.mediaUrl,
                    POSITION, position ?: index,
                    CATEGORY, "",
                    PROMO_ID, "",
                    PROMO_CODE, ""
            ))
        }
        return items
    }

    fun eventRecommendationImpression(items: List<RecommendationItemEntity>) {
        val categories = mutableListOf<Any>()
        for ((position, item) in items.withIndex()) {
            categories.add(DataLayer.mapOf(
                    ID, item.productId,
                    NAME, item.productName,
                    CREATIVE, item.productName,
                    CREATIVE_URL, item.iconUrl,
                    POSITION, position,
                    CATEGORY, item.categoryName,
                    PROMO_ID, "",
                    PROMO_CODE, ""
            ))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SUBHOME_WIDGET_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, categories))
                ))
    }

    fun eventRecommendationClick(item: RecommendationItemEntity, position: Int) {
        val categories = mutableListOf<Any>()
        categories.add(DataLayer.mapOf(
                ID, item.productId,
                NAME, item.productName,
                CREATIVE, item.productName,
                CREATIVE_URL, item.iconUrl,
                POSITION, position,
                CATEGORY, item.categoryName,
                PROMO_ID, "",
                PROMO_CODE, ""
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SUBHOME_WIDGET_CLICK,
                        TrackAppUtils.EVENT_LABEL, "${item.categoryName} - ${item.productName} - $position",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, categories))
                ))

    }

    fun eventSearchResultPageImpression(items: List<DigitalHomePageSearchCategoryModel>) {
        val categories = mutableListOf<Any>()
        for ((position, item) in items.withIndex()) {
            categories.add(DataLayer.mapOf(
                    ID, item.id,
                    NAME, item.name,
                    CREATIVE, item.name,
                    CREATIVE_URL, item.icon,
                    POSITION, position
            ))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SEARCH_RESULT_PAGE_ICON_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, categories))
                ))

    }

    fun eventSearchResultPageClick(item: DigitalHomePageSearchCategoryModel, position: Int) {
        val categories = mutableListOf<Any>()
        categories.add(DataLayer.mapOf(
                ID, item.id,
                NAME, item.name,
                CREATIVE, item.name,
                CREATIVE_URL, item.icon,
                POSITION, position
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SEARCH_RESULT_PAGE_ICON_CLICK,
                        TrackAppUtils.EVENT_LABEL, "${item.name} - $position",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, categories))
                ))

    }

    fun rechargeEnhanceEcommerceEvent(trackingDataString: String) {
        val trackingData = Gson().fromJson<Map<String, Any>>(trackingDataString, object : TypeToken<HashMap<String, Any>>() {}.type)
        val event = (trackingData[DigitaHomepageTrackingAdditionalConstant.EVENT] as? String) ?: ""
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(event, convertToBundle(trackingData)
        )
    }

    private fun convertToBundle(data: Map<String, Any>): Bundle {
        val bundle = Bundle()
        for (entry in data.entries) {
            when (val value = entry.value) {
                is String -> bundle.putString(entry.key, value)
                is Boolean -> bundle.putBoolean(entry.key, value)
                is Int -> bundle.putInt(entry.key, value)
                is Long -> bundle.putLong(entry.key, value)
                is Double -> bundle.putDouble(entry.key, value)
                is ArrayList<*> -> {
                    val list = ArrayList<Bundle>(
                            value.map {
                                (it as? Map<String, Any>)?.let { map ->
                                    return@map convertToBundle(map)
                                }
                                null
                            }.filterNotNull()
                    )
                    bundle.putParcelableArrayList(entry.key, list)
                }
            }
        }
        return bundle
    }

    companion object {
        const val ACTION_IMPRESSION = "impression"
        const val ACTION_CLICK = "click"
    }

}