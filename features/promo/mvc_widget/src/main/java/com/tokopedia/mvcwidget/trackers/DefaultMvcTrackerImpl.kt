package com.tokopedia.mvcwidget.trackers

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.mvcwidget.FollowWidgetType

open class DefaultMvcTrackerImpl:MvcTrackerImpl {
    //1 Pdp
    //16 Shop
    override fun userClickEntryPoints(shopId: String, userId: String?, @MvcSource source: Int, isTokomember:Boolean) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                if(isTokomember){
                    map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TOKOMEMBER_ENTRY_POINT
                }else{
                    map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON_ENTRY_POINT
                }

                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"

            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                if(isTokomember){
                    map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TOKOMEMBER_ENTRY_POINT
                }else{
                    map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON
                }

                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"

            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun userClickEntryPointOnMVCLockToProduct(
        shopId: String,
        userId: String?,
        source: Int,
        productId: String
    ) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_MVC_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$productId"

            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_MVC_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"

            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.BUSINESSUNIT_PROMO)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewMVCLockToProduct(
        shopId: String,
        userId: String?,
        source: Int,
        productId: String
    ) {

        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_LOCK_TO_PRODUCT_VIEW
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MVC_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$productId"

            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_LOCK_TO_PRODUCT_VIEW
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MVC_LOCK_TO_PRODUCT
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"

            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.BUSINESSUNIT_PROMO)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //3, 18
    override fun clickFollowButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_FOLLOW_WIDGET

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //4,10,19, 25
    override fun viewFollowButtonToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        if (isSuccess) {
            map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TOASTER_FOLLOW_SUCCESS
        } else {
            map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TOASTER_FOLLOW_ERROR
        }

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //5,9,20,24,44,45 - DO NOT SEND TRACKERS YET - IT IS MUTUALLY DECIDED WITH PO
    override fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }
        when (widgetType) {
            FollowWidgetType.FIRST_FOLLOW -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_SHOP_FOLLOWERS_COUPON
            }
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MEMBERSHIP_COUPON
            }
            else -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_REGULAR_COUPON
            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
//        getTracker().sendGeneralEvent(map)
    }

    //2,6,17,21
    override fun viewWidgetImpression(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }
        when (widgetType) {
            FollowWidgetType.FIRST_FOLLOW -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_FOLLOW_WIDGET
            }
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MEMBERSHIP_WIDGET
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MEMBERSHIP_WIDGET
            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //7, 22
    override fun clickJadiMemberButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_JADI_MEMBER
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //8,11,23,26
    override fun viewJadiMemberToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        if (isSuccess) {
            map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TOASTER_JADI_MEMBER_SUCCESS
        } else {
            map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TOASTER_JADI_MEMBER_ERROR
        }

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //12, 27
    override fun clickCekInfoButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CEK_INFO
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCekInfoButtonClose(@FollowWidgetType widgetType: String, shopId: String,userId: String?,@MvcSource source: Int, buttonTitle:String?){
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
        map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.MVC_CLOSE_CEK_INFO} $shopId"
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CEK_INFO

        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOMEMBER_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //13,28
    override fun viewTokomemberBottomSheet(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_INFO
        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //14, 29
    override fun clickDaftarJadiMember(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_DAFTAR

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //15, 30
    override fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_SHOP
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.SHOP_PAGE_BUYER
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLOSE_BOTTOMSHEET

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43


    //MVC close memberhsip GTM
    override fun clickLihatExpand(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER_CLOSE
        map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.MVC_CLOSE_VIEW_SELEGKAPANYA} $shopId"
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_LIHAT_SELENGKAPNYA

        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOMEMBER_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickMulaiBelanjaButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?){
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_MV
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER_CLOSE
        map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.MVC_CLOSE_VIEW_MULAIBELANJA} $shopId"
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_MULAI_BELANJA

        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOMEMBER_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    //Reward GTM for Bottomsheet CTA
    override fun userClickBottomSheetCTA(@FollowWidgetType widgetType: String, label: String, userId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_KUPON
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.REWARDS_CATEGORY
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CEK_TOKO
        map[Tracker.Constants.EVENT_LABEL] = label

        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun tokomemberImpressionOnPdp(shopId: String,userId: String?, isTokomember: Boolean){
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_MV
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
        map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.PDP_VIEW}-$shopId"
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TOKOMEMBER

        if(!isTokomember){
            map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.SEE_ENTRY_POINT
            map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.MERCHANT_VOUCHER
        }

        Tracker.fillCommonItems(map, userId, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewMVCCoupon(label: String, mapData: HashMap< String,Any> , @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_VIEW_PROMO
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.REWARDS_CATEGORY
        map[Tracker.Constants.EVENT_LABEL] = label
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_MVC_COUPON
        map[Tracker.Constants.ECOMMERCE] = DataLayer.mapOf("promoView", mapData)

        if (source == MvcSource.REWARDS) {
            Tracker.fillCommonItems(map, "", Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        }
        Tracker.getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun mvcMultiShopCardClick(
        shopName: String,
        eventAction: String,
        @MvcSource source: Int,
        userId: String?,
        productPosition:Int,
        label: String
    ) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_KUPON
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.REWARDS_CATEGORY
        map[Tracker.Constants.EVENT_ACTION] = eventAction
        map[Tracker.Constants.EVENT_LABEL] = label

        if (source == MvcSource.REWARDS) {
            Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        }
        Tracker.getTracker().sendGeneralEvent(map)
    }


}