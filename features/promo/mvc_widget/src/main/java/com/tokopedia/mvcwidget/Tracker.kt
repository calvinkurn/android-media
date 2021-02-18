package com.tokopedia.mvcwidget

import androidx.annotation.IntDef
import com.tokopedia.mvcwidget.MvcSource.Companion.PDP
import com.tokopedia.mvcwidget.MvcSource.Companion.SHOP
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

/*
* Confusing Trackers
* 2,3, 19, 20
* */

object Tracker {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val CREATIVE_SLOT = "creative_slot"
        const val PROMOTIONS = "promotions"
        const val CREATIVE_NAME = "creative_name"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
    }

    object Event {
        const val CLICK_PDP = "clickPDP"
        const val CLICK_SHOP = "clickShop"
        const val PROMO_VIEW = "promoView"
        const val CLICK_MV = "clickMerchantVoucher"
    }

    object Category {
        const val MERCHANT_VOUCHER = "merchant voucher"
        const val SHOP_PAGE_BUYER = "shop page - buyer"
    }

    object Action {
        const val CLICK_COUPON_ENTRY_POINT = "click coupon entry point"
        const val CLICK_COUPON = "click coupon"
        const val VIEW_FOLLOW_WIDGET = "view follow widget"
        const val CLICK_FOLLOW_WIDGET = "click follow widget"
        const val CLICK_JADI_MEMBER = "click jadi member"
        const val CLICK_CEK_INFO = "click cek info"
        const val CLICK_DAFTAR = "click daftar jadi member widget"
        const val CLOSE_BOTTOMSHEET = "close buttomsheet"
    }

    object Label {
        const val PDP_VIEW = "pdp view"
        const val SHOP_PAGE = "shop page"
    }

    fun fillCommonItems(map:MutableMap<String,Any>,userId:String?){
        map[Constants.BUSINESS_UNIT] = ""
        map[Constants.CURRENT_SITE] = ""
        userId?.let {
            map[Constants.USER_ID] = userId
        }
    }

    fun fillPromotions(map:MutableMap<String,Any>){
        val promoMap = HashMap<String,Any>()
        promoMap[Constants.CREATIVE_NAME] = ""
        promoMap[Constants.ITEM_ID] = ""
        promoMap[Constants.ITEM_NAME] = ""
        map[Constants.PROMOTIONS] = promoMap
    }

    fun fillCreativeSlot(map:MutableMap<String,Any>){
        map[Constants.CREATIVE_SLOT] = ""
    }

    //1 Pdp
    //16 Shop
    fun userClickEntryPoints(shopId: String, userId:String?, @MvcSource source:Int ) {
        val map = mutableMapOf<String, Any>()
        when(source){
            MvcSource.PDP-> {
                map[Constants.EVENT] = Event.CLICK_PDP
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_ACTION] = Action.CLICK_COUPON_ENTRY_POINT
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"

            }
            MvcSource.SHOP-> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_ACTION] = Action.CLICK_COUPON
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"

            }
        }
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //3, 18
    fun clickFollowButton(shopId: String,userId:String?, @MvcSource source:Int){
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_MV
        when(source){
            MvcSource.PDP->{
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP->{
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_FOLLOW_WIDGET

        fillCommonItems(map, userId)
        fillCreativeSlot(map)
        getTracker().sendGeneralEvent(map)
    }

    //7, 22
    fun clickJadiMemberButton(shopId: String,userId:String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_MV
        when(source){
            MvcSource.PDP->{
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP->{
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_JADI_MEMBER
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //12, 27
    fun clickCekInfoButton(shopId: String,userId:String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_MV
        when(source){
            MvcSource.PDP->{
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP->{
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_CEK_INFO
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //14, 29
    fun clickDaftarJadiMember(shopId: String,userId:String?, @MvcSource source: Int){
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_MV
        when(source){
            MvcSource.PDP->{
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP->{
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }
        map[Constants.EVENT_ACTION] = Action.CLICK_DAFTAR

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //15, 30
    fun closeMainBottomSheet(shopId: String,userId:String?, @MvcSource source: Int){
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_MV
        when(source){
            MvcSource.PDP->{
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP->{
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLOSE_BOTTOMSHEET

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43,44,45
}
@Retention(AnnotationRetention.SOURCE)
@IntDef(SHOP, PDP)
annotation class MvcSource {

    companion object {
        const val SHOP = 1
        const val PDP = 2
    }
}
