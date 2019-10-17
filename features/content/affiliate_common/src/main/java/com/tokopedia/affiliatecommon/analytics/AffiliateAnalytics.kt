package com.tokopedia.affiliatecommon.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject

/**
 * @author by yfsx on 05/11/18.
 */
class AffiliateAnalytics @Inject
constructor(private val userSession: UserSessionInterface) {

    val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm


    companion object {
        private val PARAM_SCREEN_NAME = "screenName"
        private val PARAM_EVENT_NAME = "event"
        private val PARAM_EVENT_CATEGORY = "eventCategory"
        private val PARAM_EVENT_ACTION = "eventAction"
        private val PARAM_EVENT_LABEL = "eventLabel"
        private val PARAM_USER_ID = "user_id"
        private val PARAM_PRODUCT_ID = "product_id"
        private val PARAM_SHOP_ID = "shop_id"
    }

    private fun setDefaultData(screenName: String,
                               event: String,
                               category: String,
                               action: String,
                               label: String): HashMap<String, Any> {
        val mapEvent = HashMap<String, Any>()
        mapEvent[PARAM_SCREEN_NAME] = screenName
        mapEvent[PARAM_EVENT_NAME] = event
        mapEvent[PARAM_EVENT_CATEGORY] = category
        mapEvent[PARAM_EVENT_ACTION] = action
        mapEvent[PARAM_EVENT_LABEL] = label
        return mapEvent
    }

    private fun setDefaultDataWithUserId(screenName: String,
                                         event: String,
                                         category: String,
                                         action: String,
                                         label: String): HashMap<String, Any> {
        val mapEvent = setDefaultData(screenName, event, category, action,
                label)
        mapEvent[PARAM_USER_ID] = userSession.userId
        return mapEvent
    }

    private fun getEnhancedEcommerceImpressions(
            productName: String, productId: String, productComission: Int, sectionName: String,
            position: Int): HashMap<String, Any> {
        val ecommerceItem = HashMap<String, Any>()
        ecommerceItem["name"] = productName
        ecommerceItem["id"] = productId
        ecommerceItem["price"] = productComission
        ecommerceItem["list"] = String.format("/affiliate explore - %s", sectionName)
        ecommerceItem["position"] = position

        val listEcommerce = ArrayList<Any>()
        listEcommerce.add(ecommerceItem)

        val ecommerce = HashMap<String, Any>()
        ecommerce["currencyCode"] = "IDR"
        ecommerce["impressions"] = listEcommerce
        return ecommerce
    }

    private fun getEnhancedEcommerceClick(
            productName: String, productId: String, productComission: Int, sectionName: String,
            position: Int): HashMap<String, Any> {
        val list = String.format("/affiliate explore - %s", sectionName)

        val productItem = HashMap<String, Any>()
        productItem["name"] = productName
        productItem["id"] = productId
        productItem["price"] = productComission
        productItem["list"] = list
        productItem["position"] = position

        val products = ArrayList<Any>()
        products.add(productItem)

        val actionField = HashMap<String, Any>()
        actionField["list"] = list

        val click = HashMap<String, Any>()
        click["actionField"] = actionField
        click["products"] = products

        val ecommerce = HashMap<String, Any>()
        ecommerce["click"] = click
        return ecommerce
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 31 & 32
    fun onSuggestionItemAppeared(productId: String, isTypeAffiliate: Boolean) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        if (isTypeAffiliate) AffiliateEventTracking.Action.IMPRESSION_SUGGESTION_AFFILIATE
                            else AffiliateEventTracking.Action.IMPRESSION_SUGGESTION_MERCHANT,
                        productId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 33 & 34
    fun onSuggestionItemClicked(productId: String, isTypeAffiliate: Boolean) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        if (isTypeAffiliate) AffiliateEventTracking.Action.CLICK_SUGGESTION_AFFILIATE
                        else AffiliateEventTracking.Action.CLICK_SUGGESTION_MERCHANT,
                        productId
                )
        )
    }


    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 3
    fun onSearchSubmitted(keyword: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "search",
                        keyword
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 4
    fun onInfoClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click info",
                        ""
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 5
    fun onProfileClicked(userId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click profile page",
                        userId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 6
    fun onBannerClicked(activityId: String, imageUrl: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click banner global announcement",
                        String.format("%s-%s", activityId, imageUrl)
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 7
    fun onQuickFilterClicked(category: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click quick filter",
                        category
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 9
    fun onProductImpression(adId: String, productName: String, productId: String, productComission: Int,
                            sectionName: String, position: Int) {
        val data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.PRODUCT_VIEW,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.IMPRESSION_PRODUCT,
                String.format("%s-%s-%s", sectionName, productId, adId)
        )
        data["ecommerce"] = getEnhancedEcommerceImpressions(productName,
                productId,
                productComission,
                sectionName,
                position)
        analyticTracker.sendEnhanceEcommerceEvent(data)
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 10
    fun onProductClicked(adId: String, productName: String, productId: String, productComission: Int,
                         sectionName: String, position: Int) {
        val data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.PRODUCT_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.CLICK_PRODUCT,
                String.format("%s-%s-%s", sectionName, productId, adId)
        )
        data["ecommerce"] = getEnhancedEcommerceClick(productName,
                productId,
                productComission,
                sectionName,
                position)
        analyticTracker.sendEnhanceEcommerceEvent(data)
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 11
    fun onPopularClicked(profileId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to other profile - most popular curation",
                        profileId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 12
    fun onSortClicked(profileId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to sort",
                        profileId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 13
    fun onFilterClicked(profileId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to filter",
                        profileId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 19
    fun onJatahRekomendasiHabisDialogShow() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "popup message jatah rekomendasi habis",
                        userSession.userId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 20
    fun onTambahGambarButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah foto",
                        userSession.userId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 21
    fun onTambahVideoButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah video",
                        userSession.userId
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 22
    fun onSelesaiCreateButtonClicked(productIds: List<String>) {
        val stringBuilder = StringBuilder()
        for (i in productIds.indices) {
            stringBuilder.append(productIds[i])
            if (i != productIds.size - 1) {
                stringBuilder.append(",")
            }
        }
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click post sekarang",
                        String.format("%s,%s", userSession.userId, stringBuilder.toString())
                )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1rHvhUjU6p3DyQxavWLKmfjTFbzZfxsItwwqfrVscGyo/edit#gid=310294407
    // screenshot no 23
    fun onTambahTagButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah tag",
                        userSession.userId
                )
        )
    }

    fun onSearchNotFound(keyword: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        AffiliateEventTracking.Action.SEARCH_NOT_FOUND,
                        keyword
                )
        )
    }

    fun onSimpanButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CLAIM,
                        AffiliateEventTracking.Action.CLICK_SIMPAN,
                        ""
                )
        )
    }

    fun onSKButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CLAIM,
                        AffiliateEventTracking.Action.CLICK_SYARAT_KETENTUAN,
                        ""
                )
        )
    }

    fun onLihatContohButtonClicked(productId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_CONTOH,
                        productId
                )
        )
    }

    fun onDirectRecommRekomendasikanButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_ADD_RECOMMENDATION,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DIRECT_RECOMM,
                        AffiliateEventTracking.Action.CLICK_REKOMENDASIKAN,
                        ""
                )
        )
    }

    fun onDirectRecommProdukLainButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_ADD_RECOMMENDATION,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DIRECT_RECOMM,
                        AffiliateEventTracking.Action.CLICK_LIHAT_PRODUK_LAINNYA,
                        ""
                )
        )
    }

    fun onDirectRecommPilihanProdukButtonClicked() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_PILIHAN_PRODUK,
                        ""
                )
        )
    }

    fun onAfterClickTokopediMe(originalLink: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_USER_PROFILE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_AFFILIATE_TRAFFIC,
                        AffiliateEventTracking.Action.OTHERS,
                        originalLink
                )
        )
    }

    fun onAfterClickContentDetail(contentId: String) {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CONTENT_DETAIL,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_AFFILIATE_CDP_EXTERNAL,
                        AffiliateEventTracking.Action.CLICK_AFFILIATE_CDP_EXTERNAL,
                        contentId
                )
        )
    }

    fun onAfterClickSaldo() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_MY_PROFILE,
                        AffiliateEventTracking.Event.PROFILE_CLICK,
                        AffiliateEventTracking.Category.BYME_MY_PROFILE,
                        AffiliateEventTracking.Action.CLICK_TOKOPEDIA_SALDO,
                        ""
                )
        )
    }

    fun onImpressionOnboard() {
        analyticTracker.sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.IMPRESSION_ONBOARD,
                        ""
                )
        )
    }

    fun onAutoCompleteClicked(suggestionText: String, keyword: String) {
        analyticTracker.sendGeneralEvent(AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.CLICK_SEARCH_SUGGESTION,
                String.format("%s-%s", suggestionText, keyword))
    }

    fun trackProductImpressionSearchResult(keyword: String, adId: String, productName: String, productId: String, productComission: Int, position: Int) {
        val data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.IMPRESSION_PRODUCT,
                String.format("%s-%s-%s-%s", AffiliateEventTracking.EventLabel.SEARCH_RESULT, productId, keyword, adId)
        )
        data["ecommerce"] = getEnhancedEcommerceImpressions(productName,
                productId,
                productComission,
                AffiliateEventTracking.EventLabel.SEARCH_RESULT,
                position)
        analyticTracker.sendEnhanceEcommerceEvent(data)
    }

    fun onProductSearchClicked(keyword: String, adId: String, productName: String, productId: String, productComission: Int, position: Int) {
        val data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.CLICK_PRODUCT,
                String.format("%s-%s-%s-%s", AffiliateEventTracking.EventLabel.SEARCH_RESULT, productId, keyword, adId)
        )
        data["ecommerce"] = getEnhancedEcommerceImpressions(productName,
                productId,
                productComission,
                AffiliateEventTracking.EventLabel.SEARCH_RESULT,
                position)
        analyticTracker.sendEnhanceEcommerceEvent(data)
    }
}
