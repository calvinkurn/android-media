package com.tokopedia.digital.home.presentation.Util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.ID
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.NAME
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.POSITION
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.ALL_BANNERS_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BACK_BUTTON_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.MORE_INFO_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.NEW_USER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SEARCH_BOX_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SEARCH_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SUBHOME_WIDGET_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SUBHOME_WIDGET_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingCategoryConstant.DIGITAL_HOMEPAGE_CATEGORY
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.CLICK_HOMEPAGE
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.FAVOURITE_NUMBER
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.HELP
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.LANGGANAN
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.ORDER_LIST
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class DigitalHomeTrackingUtil {

    private var initialImpressionTracking: MutableMap<String, Boolean> = initialImpressionTrackingConst.toMutableMap()

    fun eventBannerImpression(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.title,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.title,
                CREATIVE_URL, item?.filename
        ))

        val eventAction = BANNER_IMPRESSION
        initialImpressionTracking.apply {
            if (containsKey(eventAction)) {
                if (this[eventAction] == false) {
                    // Disable initial load after first time
                    this[eventAction] = true
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                            DataLayer.mapOf(
                                    TrackAppUtils.EVENT, PROMO_VIEW,
                                    TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                                    TrackAppUtils.EVENT_ACTION, BANNER_IMPRESSION,
                                    TrackAppUtils.EVENT_LABEL, "$position - ${item?.title}",
                                    ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                            ))
                }
            }
        }
    }

    fun eventBannerClick(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.title,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.title,
                CREATIVE_URL, item?.filename
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
                    NAME, item?.name,
                    POSITION, index,
                    ID, item?.id,
                    CREATIVE, item?.name,
                    CREATIVE_URL, item?.icon
            ))
        }

        val eventAction = DYNAMIC_ICON_IMPRESSION
        initialImpressionTracking.apply {
            if (containsKey(eventAction)) {
                if (this[eventAction] == false) {
                    // Disable initial load after first time
                    this[eventAction] = true
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                            DataLayer.mapOf(
                                    TrackAppUtils.EVENT, PROMO_VIEW,
                                    TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                                    TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_IMPRESSION,
                                    TrackAppUtils.EVENT_LABEL, "",
                                    ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                            ))
                }
            }
        }
    }

    fun eventCategoryClick(item: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.name,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.name,
                CREATIVE_URL, item?.icon
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
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, ORDER_LIST)
    }

    fun eventClickLangganan(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, LANGGANAN)
    }

    fun eventClickHelp(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, HELP)
    }

    fun eventClickFavNumber(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, FAVOURITE_NUMBER)
    }

    fun eventClickBackButton(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, BACK_BUTTON_CLICK, "")
    }

    fun eventClickSearchBox(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_BOX_CLICK, "")
    }

    fun eventClickSearch(searchQuery: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_CLICK, searchQuery)
    }

    fun eventClickAllBanners(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, ALL_BANNERS_CLICK, "")
    }

    fun eventClickSubscriptionGuide(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, SUBSCRIPTION_GUIDE_CLICK, "")
    }

    fun eventClickMoreInfo(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, MORE_INFO_CLICK, "")
    }

    fun eventSectionImpression(data: List<DigitalHomePageSectionModel.Item>, eventAction: String) {
        initialImpressionTracking.apply {
            if (containsKey(eventAction)) {
                if (this[eventAction] == false) {
                    // Disable initial load after first time
                    this[eventAction] = true
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                            DataLayer.mapOf(
                                    TrackAppUtils.EVENT, PROMO_VIEW,
                                    TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                                    TrackAppUtils.EVENT_ACTION, eventAction,
                                    TrackAppUtils.EVENT_LABEL, "",
                                    ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, createSectionItem(data).toArray()))
                            ))
                }
            }
        }
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
                    POSITION, position ?: index,
                    CREATIVE, item.title,
                    CREATIVE_URL, item.mediaUrl
            ))
        }
        return items
    }

    fun eventRecommendationImpression(items: List<RecommendationItemEntity>) {
        val categories = mutableListOf<Any>()
        for ((position, item) in items.withIndex()) {
            categories.add(DataLayer.mapOf(
                    NAME, item.productName,
                    POSITION, position,
                    ID, item.productId,
                    CREATIVE, item.productName,
                    CREATIVE_URL, item.iconUrl
            ))
        }


        val eventAction = SUBHOME_WIDGET_IMPRESSION
        initialImpressionTracking.apply {
            if (containsKey(eventAction)) {
                if (this[eventAction] == false) {
                    // Disable initial load after first time
                    this[eventAction] = true
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                            DataLayer.mapOf(
                                    TrackAppUtils.EVENT, PROMO_VIEW,
                                    TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                                    TrackAppUtils.EVENT_ACTION, SUBHOME_WIDGET_IMPRESSION,
                                    TrackAppUtils.EVENT_LABEL, "",
                                    ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, categories))
                            ))
                }
            }
        }

    }

    fun eventRecommendationClick(item: RecommendationItemEntity, position: Int) {
        val categories = mutableListOf<Any>()
        categories.add(DataLayer.mapOf(
                NAME, item.productName,
                POSITION, position,
                ID, item.productId,
                CREATIVE, item.productName,
                CREATIVE_URL, item.iconUrl
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
                    NAME, item.name,
                    POSITION, position,
                    ID, item.id,
                    CREATIVE, item.name,
                    CREATIVE_URL, item.icon
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
                    NAME, item.name,
                    POSITION, position,
                    ID, item.id,
                    CREATIVE, item.name,
                    CREATIVE_URL, item.icon
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

    fun resetInitialImpressionTracking() {
        initialImpressionTracking = initialImpressionTrackingConst.toMutableMap()
    }

    companion object {
        val initialImpressionTrackingConst = mapOf(
                DIGITAL_HOMEPAGE_CATEGORY to false,
                BANNER_IMPRESSION to false,
                BEHAVIORAL_CATEGORY_IMPRESSION to false,
                NEW_USER_IMPRESSION to false,
                SPOTLIGHT_IMPRESSION to false,
                SUBHOME_WIDGET_IMPRESSION to false
        )
    }

}