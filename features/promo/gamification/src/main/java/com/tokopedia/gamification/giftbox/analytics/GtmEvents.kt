package com.tokopedia.gamification.giftbox.analytics

import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

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
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //2
    fun clickRewardDetail(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_REWARD_DETAIL
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //3
    fun clickInfoButtonFromDialog(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_T_AND_C
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //4
    fun clickProductRecom(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_PRODUCT_RECOM
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
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
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        val loginText:String = if (userId.isNullOrEmpty()) {
            "nonlogin"
        } else {
            "login"
        }
        map[GiftBoxTrackerConstants.ITEM_LIST] = "/tap-tap - $loginText - rekomendasi untuk anda - $recommendationType - $isTopAds"
        map[GiftBoxTrackerConstants.ITEMS] = getItemsMap(productId,
                productPositionIndex,
                productBrand,
                itemCategory,
                productName,
                productVariant,
                productPrice)

        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    private fun getItemsMap(productId: String,
                            productPositionIndex:Int,
                            productBrand:String,
                            itemCategory:String,
                            productName:String,
                            productVariant:String,
                            productPrice:String):Map<String,Any>{

        val itemsMap = mutableMapOf<String, Any>()
        itemsMap["index"] = productPositionIndex
        itemsMap["item_brand"] = productBrand
        itemsMap["item_category"] = itemCategory
        itemsMap["item_id"] = productId
        itemsMap["item_name"] = productName
        itemsMap["item_variant"] = productVariant
        itemsMap["price"] = productPrice
        return itemsMap
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
        map[GiftBoxTrackerConstants.BUSINESS_UNIT] = GiftBoxTrackerConstants.BGP_ENGAGEMENT
        map[GiftBoxTrackerConstants.CURRENT_SITE] = GiftBoxTrackerConstants.TOKOPEDIA_MARKET_PLACE
        val loginText = if (userId.isNullOrEmpty()) {
            "nonlogin"
        } else {
            "login"
        }

        map[GiftBoxTrackerConstants.ITEM_LIST] = "/tap-tap - $loginText - rekomendasi untuk anda - $recommendationType - $isTopAds"
        map[GiftBoxTrackerConstants.ITEMS] = getItemsMap(productId,
                productPositionIndex,
                productBrand,
                itemCategory,
                productName,
                productVariant,
                productPrice)

        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }
}