package com.tokopedia.topads.common.analytics

import com.tokopedia.topads.common.data.model.InsightDailyBudgetModel
import com.tokopedia.topads.common.data.model.InsightProductRecommendationModel
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.interfaces.Analytics

const val ON = "on"
const val OFF = "off"
const val EVENT_ACTION_CLICK_AKTIFKAN_ATUR_MANUAL = "click aktifkan atur manual"
const val EVENT_ACTION_CLICK_BATALKAN_ATUR_MANUAL = "click batalkan atur manual"
const val EVENT_ACTION_CLICK_BATALKAN_ATUR_OTOMATIS = "click batalkan atur otomatis"
const val EVENT_ACTION_CLICK_AKTIFKAN_ATUR_OTOMATIS = "click aktifkan atur otomatis"
const val EVENT_ACTION_CLICK_TOGGLE_ATUR_OTOMATIS = "click toggle atur otomatis"
private const val CLICK_TOP_ADS = "clickTopAds"
private const val KEY_EVENT_SCREEN_NAME = "screenName"
private const val KEY_OPEN_SCREEN_EVENT = "openScreen"
private const val KEY_EVENT_CATEGORY_VALUE = "ads creation form"
private const val KEY_EVENT_CATEGORY_VALUE_EDIT = "edit group form"
private const val KEY_EVENT_VALUE_EDIT = "clickEditGroup"
private const val KEY_EVENT_VALUE = "clickAdsCreation"
private const val KEY_EVENT_DASHBOARD_VALUE = "clickAutoAds"
private const val KEY_EVENT_DASHBOARD_CATEGORY_VALUE = "auto ads dashboard"
private const val KEY_TOP_ADS_SCREEN_NAME = "/topads - home"
private const val KEY_TOP_ADS_OBAORDING_SCREEN_NAME = "/autoads - onboarding"
private const val KEY_EVENT_LOGGED_IN_STATUS = "isLoggedInStatus"
private const val KEY_EVENT_INSIGHT_RECOMMENDATION = "clickShopInsight"
private const val KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION = "Insight center"
private const val KEY_EVENT_PDP_BOTTOMSHEET = "clickTopAds"
private const val KEY_EVENT_CATEGORY_PDP_BOTTOMSHEET = "bottomsheet"
private const val KEY_EVENT_EDIT_FORM = "clickTopAdsEditForm"
private const val KEY_EVENT_CATEGORY_EDIT_FORM = "edit form mobile"
private const val KEY_EVENT_HEADLINE_ADS = "clickTopAds"
private const val KEY_EVENT_CLICK_ADS_CREATE = "clickAdsCreation"
private const val KEY_EVENT_HEADLINE_CREATE_FORM = "viewAdsCreationIris"
private const val KEY_EVENT_IRIS_ADS = "viewTopAdsIris"
private const val KEY_EVENT_CATEGORY_HEADLINE_ADS = "headline ads dashboard"
private const val KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION = "headline ads creation"
private const val KEY_BUSINESS_UNIT = "ads solution"
private const val KEY_CURRENT_SITE = "tokopediamarketplace"
private const val KEY_PROMO_CLICK = "promoClick"
private const val KEY_PROMO_VIEW = "promoView"
private const val KEY_EVENT_GROUP_DETAIL = "topAdsSellerApp"
private const val KEY_EVENT_CATEGORY_GROUP_DETAIL = "topads detail group iklan"
private const val KEY_EVENT_CATEGORY_IKLAN_PRODUK = "topads dashboard iklan produk"
private const val KEY_EVENT_CATEGORY_PRODUCT_CREATE = "topads manage product create form"
private const val KEY_EVENT_CATEGORY_PRODUCT_EDIT = "topads manage product edit form"
private const val KEY_EVENT_CATEGORY_ONBOARDING = "onboarding dashboard"
const val TRACKER_ID_CLICK_TOGGLE_ATUR_OTOMATIS = "33130"
const val TRACKER_ID_CLICK_AKTIFKAN_ATUR_OTOMATIS = "33131"
const val TRACKER_ID_CLICK_BATALKAN_ATUR_OTOMATIS = "33132"
const val TRACKER_ID_CLICK_AKTIFKAN_ATUR_MANUAL = "33133"
const val TRACKER_ID_CLICK_BATALKAN_ATUR_MANUAL = "33134"

class TopAdsCreateAnalytics : BaseTrackerConst() {

    companion object {
        val topAdsCreateAnalytics: TopAdsCreateAnalytics by lazy { TopAdsCreateAnalytics() }
    }

    fun sendTopAdsEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_VALUE,
                Category.KEY to KEY_EVENT_CATEGORY_VALUE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEventEdit(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_VALUE_EDIT,
                Category.KEY to KEY_EVENT_CATEGORY_VALUE_EDIT,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_VALUE,
                Category.KEY to KEY_EVENT_CATEGORY_VALUE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsDashboardEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_DASHBOARD_VALUE,
                Category.KEY to KEY_EVENT_DASHBOARD_CATEGORY_VALUE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenOnboardingScreenEvent(isLoggedInStatus: Boolean, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_OBAORDING_SCREEN_NAME,
                KEY_EVENT_LOGGED_IN_STATUS to isLoggedInStatus.toString(),
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenScreenEvent() {
        val map = mapOf(
                Event.KEY to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_SCREEN_NAME
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendInsightGtmEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_INSIGHT_RECOMMENDATION,
                Category.KEY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendPdpBottomSheetEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_PDP_BOTTOMSHEET,
                Category.KEY to KEY_EVENT_CATEGORY_PDP_BOTTOMSHEET,
                Action.KEY to eventAction,
                Label.KEY to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendEditFormEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_EDIT_FORM,
                Category.KEY to KEY_EVENT_CATEGORY_EDIT_FORM,
                Action.KEY to eventAction,
                Label.KEY to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendEditFormSaveEvent(eventAction: String, map: MutableList<MutableMap<String, String>>) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_EDIT_FORM,
                Category.KEY to KEY_EVENT_CATEGORY_EDIT_FORM,
                Action.KEY to eventAction,
                Label.KEY to map)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineAdsEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_HEADLINE_ADS,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineAdsViewEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_IRIS_ADS,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendViewFormEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_IRIS_ADS,
            Action.KEY to eventAction,
            Category.KEY to KEY_EVENT_CATEGORY_PRODUCT_EDIT,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to ""
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_HEADLINE_CREATE_FORM,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormClickEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_CLICK_ADS_CREATE,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<TopAdsProductModel>, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_VIEW,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                Promotion.KEY to getProductList(data)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceCLickEvent(eventAction: String, eventLabel: String, data: List<TopAdsProductModel>, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_CLICK,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_CLICK to mapOf(
                                Promotion.KEY to getProductList(data)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getProductList(products: List<TopAdsProductModel>): Any? {
        var list = arrayListOf<Any>()
        products.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to it.productID.toString(),
                    "name" to it.productName,
                    "creative" to  it.productName,
                    "position" to index + 1))
        }
        return list
    }


    fun sendHeadlineCreatFormEcommerceKeywordViewEvent(eventAction: String, eventLabel: String, data: List<KeywordDataItem>, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_VIEW,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                Promotion.KEY to getKeywordList(data)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceKeywordCLickEvent(eventAction: String, eventLabel: String, data: List<KeywordDataItem>, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_CLICK,
                Category.KEY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_CLICK to mapOf(
                                Promotion.KEY to getKeywordList(data)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun getKeywordList(keywords: List<KeywordDataItem>): Any? {
        var list = arrayListOf<Any>()
        keywords.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to "",
                    "name" to it.keyword,
                    "creative" to it.bidSuggest + " - " + it.competition,
                    "position" to index + 1))
        }
        return list
    }

    fun sendInsightShopEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_EVENT_INSIGHT_RECOMMENDATION,
                Category.KEY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                UserId.KEY to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendInsightSightProductEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<InsightProductRecommendationModel>, position: Int, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_VIEW,
                Category.KEY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                Promotion.KEY to getInsightProductList(data, position)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendInsightSightDailyProductEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<InsightDailyBudgetModel>, position: Int, userId: String) {
        val map = mapOf(
                Event.KEY to KEY_PROMO_VIEW,
                Category.KEY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                BusinessUnit.KEY to KEY_BUSINESS_UNIT,
                CurrentSite.KEY to KEY_CURRENT_SITE,
                Action.KEY to eventAction,
                Label.KEY to eventLabel,
                Ecommerce.KEY to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                Promotion.KEY to getInsightDailyBidgetList(data, position)
                        )),
                UserId.KEY to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getInsightDailyBidgetList(data: List<InsightDailyBudgetModel>, position: Int): Any? {
        var list = arrayListOf<Any>()
        data.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to it.id,
                    "name" to it.name,
                    "creative" to "${it.dailySuggestedPrice} - ${it.potentialClick}",
                    "position" to position+1))
        }
        return list
    }

    private fun getInsightProductList(data: List<InsightProductRecommendationModel>, position: Int): Any? {
        var list = arrayListOf<Any>()
        data.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to it.id,
                    "name" to it.name,
                    "creative" to "${it.searchNumber} - ${it.searchPercent} - ${it.recommendedBid}",
                    "position" to position+1))
        }
        return list
    }

    fun sendAutoBidToggleTopAdsGroupDetailEvent(eventAction: String, eventLabel: String, trackerId: String) {
        val map = mapOf(
            Event.KEY to CLICK_TOP_ADS,
            Category.KEY to KEY_EVENT_CATEGORY_PRODUCT_EDIT,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to KEY_BUSINESS_UNIT,
            CurrentSite.KEY to KEY_CURRENT_SITE,
            TrackerId.KEY to trackerId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsGroupDetailEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_GROUP_DETAIL,
            Category.KEY to KEY_EVENT_CATEGORY_GROUP_DETAIL,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsGroupEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_GROUP_DETAIL,
            Category.KEY to KEY_EVENT_CATEGORY_IKLAN_PRODUK,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }

    fun sendAutoAdsEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_DASHBOARD_VALUE,
            Category.KEY to KEY_EVENT_DASHBOARD_CATEGORY_VALUE,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsCreateEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_HEADLINE_ADS,
            Category.KEY to KEY_EVENT_CATEGORY_PRODUCT_CREATE,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEditEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_HEADLINE_ADS,
            Category.KEY to KEY_EVENT_CATEGORY_PRODUCT_EDIT,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsCreateOnboardingEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
            Event.KEY to KEY_EVENT_HEADLINE_ADS,
            Category.KEY to KEY_EVENT_CATEGORY_ONBOARDING,
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "")

        getTracker().sendGeneralEvent(map)
    }


    fun sendKeywordAddEvent(eventAction: String, eventLabel: String, data: List<KeywordDataItem>) {
        val map = mapOf(
            Event.KEY to KEY_PROMO_CLICK,
            Category.KEY to KEY_EVENT_CATEGORY_PRODUCT_CREATE,
            BusinessUnit.KEY to "",
            CurrentSite.KEY to "",
            Action.KEY to eventAction,
            Label.KEY to eventLabel,
            Ecommerce.KEY to mapOf(
                KEY_PROMO_CLICK to mapOf(
                    Promotion.KEY to getAddedKeywordList(data)
                )))

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getAddedKeywordList(data: List<KeywordDataItem>): Any? {
        var list = arrayListOf<Any>()
        data.forEachIndexed { index, it ->
            list.add(mapOf(
                "id" to "",
                "name" to it.keyword,
                "creative" to it.totalSearch,
                "position" to it.competition))
        }

        return list
    }

}


