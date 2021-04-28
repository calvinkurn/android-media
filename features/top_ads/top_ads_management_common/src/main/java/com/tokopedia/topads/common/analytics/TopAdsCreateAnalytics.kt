package com.tokopedia.topads.common.analytics

import com.tokopedia.topads.common.data.model.InsightDailyBudgetModel
import com.tokopedia.topads.common.data.model.InsightProductRecommendationModel
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics


private const val KEY_EVENT = "event"
private const val KEY_EVENT_SCREEN_NAME = "screenName"
private const val KEY_OPEN_SCREEN_EVENT = "openScreen"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val KEY_EVENT_CATEGORY_VALUE = "ads creation form"
private const val KEY_EVENT_CATEGORY_VALUE_EDIT = "edit group form"
private const val KEY_EVENT_VALUE_EDIT = "clickEditGroup"
private const val KEY_EVENT_VALUE = "clickAdsCreation"
private const val KEY_EVENT_DASHBOARD_VALUE = "clickAutoAds"
private const val KEY_EVENT_DASHBOARD_CATEGORY_VALUE = "auto ads dashboard"
private const val KEY_TOP_ADS_SCREEN_NAME = "/topads - home"
private const val KEY_TOP_ADS_OBAORDING_SCREEN_NAME = "/autoads - onboarding"
private const val KEY_EVENT_LOGGED_IN_STATUS = "isLoggedInStatus"
private const val KEY_EVENT_USER_ID = "userId"
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
private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
private const val KEY_CURRENT_SITE_EVENT = "currentSite"
private const val KEY_ECOMMERCE_EVENT = "ecommerce"
private const val KEY_PROMO_CLICK = "promoClick"
private const val KEY_PROMO_VIEW = "promoView"
private const val KEY_PROMOTIONS = "promotions"
private const val KEY_CREATIVE_RECOMMENDATION_PRODUK = "jumlah pencarian - potensi tampil - rekomendasi biaya"
private const val KEY_CREATIVE_DAILY_RECOMMENDATION_PRODUK = "rekomendasi anggaran - potensi klik"


class TopAdsCreateAnalytics {

    companion object {
        val topAdsCreateAnalytics: TopAdsCreateAnalytics by lazy { TopAdsCreateAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }


    fun sendTopAdsEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEventEdit(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE_EDIT,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE_EDIT,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsDashboardEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_DASHBOARD_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_DASHBOARD_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenOnboardingScreenEvent(isLoggedInStatus: Boolean, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_OBAORDING_SCREEN_NAME,
                KEY_EVENT_LOGGED_IN_STATUS to isLoggedInStatus.toString(),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenScreenEvent() {
        val map = mapOf(
                KEY_EVENT to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_SCREEN_NAME
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendInsightGtmEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_INSIGHT_RECOMMENDATION,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendPdpBottomSheetEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_PDP_BOTTOMSHEET,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_PDP_BOTTOMSHEET,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendEditFormEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_EDIT_FORM,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_EDIT_FORM,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendEditFormSaveEvent(eventAction: String, map: MutableList<MutableMap<String, String>>) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_EDIT_FORM,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_EDIT_FORM,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to map)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineAdsEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_HEADLINE_ADS,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineAdsViewEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_IRIS_ADS,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_HEADLINE_CREATE_FORM,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormClickEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_CLICK_ADS_CREATE,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<TopAdsProductModel>, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to getProductList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceCLickEvent(eventAction: String, eventLabel: String, data: List<TopAdsProductModel>, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_PROMO_CLICK,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_CLICK to mapOf(
                                KEY_PROMOTIONS to getProductList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

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
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to getKeywordList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendHeadlineCreatFormEcommerceKeywordCLickEvent(eventAction: String, eventLabel: String, data: List<KeywordDataItem>, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_PROMO_CLICK,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_HEADLINE_ADS_CREATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_CLICK to mapOf(
                                KEY_PROMOTIONS to getKeywordList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

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
                KEY_EVENT to KEY_EVENT_INSIGHT_RECOMMENDATION,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendInsightSightProductEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<InsightProductRecommendationModel>, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to getInsightProductList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendInsightSightDailyProductEcommerceViewEvent(eventAction: String, eventLabel: String, data: List<InsightDailyBudgetModel>, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                KEY_BUSINESS_UNIT_EVENT to KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT to KEY_CURRENT_SITE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_ECOMMERCE_EVENT to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to getInsightDailyBidgetList(data)
                        )),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getInsightDailyBidgetList(data: List<InsightDailyBudgetModel>): Any? {
        var list = arrayListOf<Any>()
        data.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to it.groupId,
                    "name" to it.groupName,
                    "creative" to "${it.dailySuggestedPrice}-${it.potentialClick}",
                    "position" to index + 1))
        }
        return list
    }

    private fun getInsightProductList(data: List<InsightProductRecommendationModel>): Any? {
        var list = arrayListOf<Any>()
        data.forEachIndexed { index, it ->
            list.add(mapOf(
                    "id" to it.productid,
                    "name" to it.productname,
                    "creative" to "${it.searchCount}-${it.serachPercentage}-${it.recommendedBid}",
                    "position" to index + 1))
        }
        return list
    }
}