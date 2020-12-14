package com.tokopedia.digital.home.old.presentation.util

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant
import com.tokopedia.digital.home.old.model.*
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.CATEGORY
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.ID
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.NAME
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.POSITION
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.PROMO_CODE
import com.tokopedia.digital.home.old.presentation.util.DigitaHomepageTrackingEEConstant.PROMO_ID
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.ALL_BANNERS_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BACK_BUTTON_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.MORE_INFO_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SEARCH_BOX_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SEARCH_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBHOME_WIDGET_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBHOME_WIDGET_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingCategoryConstant.DIGITAL_HOMEPAGE_CATEGORY
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingEventNameConstant.CLICK_TOPUP_BILLS
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingLabelConstant.FAVOURITE_NUMBER
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingLabelConstant.HELP
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingLabelConstant.LANGGANAN
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingLabelConstant.ORDER_LIST
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class DigitalHomeTrackingUtil {

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

    fun eventClickOrderList(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, ORDER_LIST)
    }

    fun eventClickLangganan(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, LANGGANAN)
    }

    fun eventClickHelp(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, HELP)
    }

    fun eventClickFavNumber(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, FAVOURITE_NUMBER)
    }

    fun eventClickBackButton(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, BACK_BUTTON_CLICK, "")
    }

    fun eventClickSearchBox(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_BOX_CLICK, "")
    }

    fun eventClickSearch(searchQuery: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_CLICK, searchQuery)
    }

    fun eventClickAllBanners(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, ALL_BANNERS_CLICK, "")
    }

    fun eventClickSubscriptionGuide(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SUBSCRIPTION_GUIDE_CLICK, "")
    }

    fun eventClickMoreInfo(){
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
                    POSITION, position,
                    CATEGORY, "",
                    PROMO_ID, "",
                    PROMO_CODE, ""
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
                    POSITION, position,
                    CATEGORY, "",
                    PROMO_ID, "",
                    PROMO_CODE, ""
            ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SEARCH_RESULT_PAGE_ICON_CLICK,
                        TrackAppUtils.EVENT_LABEL, item.name,
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, categories))
                ))

    }

    fun sliceOpenApp(userId: String){
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, "clickGAMain",
                TrackAppUtils.EVENT_CATEGORY, "ga main app",
                TrackAppUtils.EVENT_ACTION, "click open app button",
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ))
    }

    fun onOpenPageFromSlice() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                RechargeAnalytics.EVENT_KEY, "openScreen",
                RechargeAnalytics.EVENT_SCREEN_NAME, "recharge homepage - from voice search - mainapp"
        ))
    }

    companion object{
        const val USER_ID = "userId"
        const val CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"
    }

}