package com.tokopedia.gamification.giftbox.analytics

import android.os.Bundle
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import timber.log.Timber

object GtmEvents {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3
    fun viewGiftBoxPage(campaignSlug: String, userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = campaignSlug
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    fun emptyBoxImpression(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.ALREADY_OPENED
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //5,14
    fun clickBackButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_BACK_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.MAIN_PAGE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //6
    fun clickShareButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SHARE_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun clickGiftBox(campaignSlug: String, userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_GIFT_BOX
        map[GiftBoxTrackerConstants.EVENT_LABEL] = campaignSlug
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //8 && 13
    fun viewRewards(benefitType: String?,catalogId: String, userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        var eventLabelPrefix = "coupon"
        if(benefitType!=null && benefitType == BenefitType.COUPON_RP_0){
            eventLabelPrefix = "produk gratis"
        }
        map[GiftBoxTrackerConstants.EVENT_LABEL] = "$eventLabelPrefix - $catalogId"
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //9
    fun viewRewardsPoints(@BenefitType benefitType: String? ,pointsAmount: String, userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        var eventLabelPrefix = "points"
        if(benefitType!=null && benefitType == BenefitType.REWARD_POINT){
            eventLabelPrefix = "tokopoints"
        }
        map[GiftBoxTrackerConstants.EVENT_LABEL] = "$eventLabelPrefix - $pointsAmount"
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //10
    fun clickClaimButton(buttonTitle: String, userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CLAIM_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = buttonTitle
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //11
    fun clickReminderButton(userId: String?, label: String?) {
        val map = mutableMapOf<String, Any>()
        if (!label.isNullOrEmpty()) {
            map[GiftBoxTrackerConstants.EVENT_LABEL] = label
        }
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_REMINDER_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //16
    fun clickExitButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.CONNECTION_ERROR
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //17
    fun clickTryAgainButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TRY_AGAIN_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //18
    fun clickSettingsButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SETTING_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //19
    fun clickToaster(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TOASTER_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //20
    fun viewNoInternetError(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_ERROR
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.INTERNET_CONN_ERROR
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //21
    fun viewToastError(userId: String?, errorMessage: String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_ERROR
        map[GiftBoxTrackerConstants.EVENT_LABEL] = errorMessage
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //1
    fun clickInfoButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_INFO_BUTTON
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    //2
    fun clickRewardDetail(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_REWARD_DETAIL
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    //3
    fun clickInfoButtonFromDialog(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_T_AND_C
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    //4
    fun clickProductRecom(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_PRODUCT_RECOM
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    fun clickProductRecomItem(userId: String?, productId: String,
                              recommendationType: String,
                              productPositionIndex:Int,
                              productBrand:String,
                              itemCategory:String,
                              productName:String,
                              productVariant:String,
                              productPrice:String,
                              isTopAds:Boolean
    ) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.SELECT_CONTENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_PRODUCT
        map[GiftBoxTrackerConstants.EVENT_LABEL] = productId
        updateCommonItems(userId,map)
        val loginText:String = if (userId.isNullOrEmpty()) {
            "nonlogin"
        } else {
            "login"
        }
        map[GiftBoxTrackerConstants.ITEM_LIST] = "/tap-tap - $loginText - rekomendasi untuk anda - $recommendationType - $isTopAds"
        map[GiftBoxTrackerConstants.ITEMS] = getItemsMapList(productId,
                productPositionIndex,
                productBrand,
                itemCategory,
                productName,
                productVariant,
                productPrice)

        try {
            getTracker().sendEnhanceEcommerceEvent(map[GiftBoxTrackerConstants.EVENT] as String, convertToBundle(map))
        }catch (th:Throwable){
            Timber.e(th)
        }
    }

    private fun getItemsMapList(productId: String,
                            productPositionIndex:Int,
                            productBrand:String,
                            itemCategory:String,
                            productName:String,
                            productVariant:String,
                            productPrice:String):List<Map<String,Any>>{

        val itemsMap = HashMap<String, Any>()
        itemsMap["index"] = productPositionIndex
        itemsMap["item_brand"] = productBrand
        itemsMap["item_category"] = itemCategory
        itemsMap["item_id"] = productId
        itemsMap["item_name"] = productName
        itemsMap["item_variant"] = productVariant
        itemsMap["price"] = productPrice
        return arrayListOf<Map<String, Any>>(itemsMap)
    }

    fun impressionProductRecomItem(userId: String?,productId: String,
                                   recommendationType: String,
                                   productPositionIndex:Int,
                                   productBrand:String,
                                   itemCategory:String,
                                   productName:String,
                                   productVariant:String,
                                   productPrice:String,
                                   isTopAds:Boolean) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_ITEM_LIST
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_PRODUCT
        map[GiftBoxTrackerConstants.EVENT_LABEL] = productId
        updateCommonItems(userId,map)
        val loginText = if (userId.isNullOrEmpty()) {
            "nonlogin"
        } else {
            "login"
        }

        map[GiftBoxTrackerConstants.ITEM_LIST] = "/tap-tap - $loginText - rekomendasi untuk anda - $recommendationType - $isTopAds"
        map[GiftBoxTrackerConstants.ITEMS] = getItemsMapList(productId,
                productPositionIndex,
                productBrand,
                itemCategory,
                productName,
                productVariant,
                productPrice)

        try {
            getTracker().sendEnhanceEcommerceEvent(map[GiftBoxTrackerConstants.EVENT] as String, convertToBundle(map))
        }catch (th:Throwable){
            Timber.e(th)
        }
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
                is List<*> -> {
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

    //1
    fun clickGreenCtaOnBackButtonDialog(userId: String?){
        val map = mutableMapOf<String,Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CEK_DAFTAR_HADIAH_BUTTON
        updateCommonItemsForBackButtonDialog(map)
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    //2
    fun clickCancelCtaOnBackButtonDialog(userId: String?){
        val map = mutableMapOf<String,Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_KE_HOME_BUTTON
        updateCommonItemsForBackButtonDialog(map)
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    //3
    fun impressionRpZeroDialog(userId: String?){
        val map = mutableMapOf<String,Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_RP_0_POP_UP
        updateCommonItemsForBackButtonDialog(map)
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

    private fun updateCommonItemsForBackButtonDialog(map:MutableMap<String,Any>){
        map[GiftBoxTrackerConstants.EVENT_LABEL] = ""
    }

    private fun updateCommonItems(userId: String?,map:MutableMap<String,Any>){
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
    }

    fun openDailyGiftBoxPage(userId: String?){
        val map = mutableMapOf<String,String>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.OPEN_SCREEN
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendScreenAuthenticated(GiftBoxTrackerConstants.DAILY_GIFT_BOX,map)
    }

    fun impressionProductRecom(userId: String?){
        val map = mutableMapOf<String,Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_PRODUCT_RECOM
        updateCommonItems(userId,map)
        getTracker().sendGeneralEvent(map)
    }

}