package com.tokopedia.mvcwidget.trackers

import com.tokopedia.mvcwidget.FollowWidgetType

interface MvcTrackerImpl {
    //1 Pdp
    //16 Shop
    fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        @MvcSource source: Int,
        isTokomember: Boolean,
        productId: String
    )

    //3, 18
    fun clickFollowButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?)

    //4,10,19, 25
    fun viewFollowButtonToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean)

    //5,9,20,24,44,45 - DO NOT SEND TRACKERS YET - IT IS MUTUALLY DECIDED WITH PO
    fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int)

    //2,6,17,21
    fun viewWidgetImpression(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int)

    //7, 22
    fun clickJadiMemberButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?)

    //8,11,23,26
    fun viewJadiMemberToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean)

    //12, 27
    fun clickCekInfoButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?)

    fun clickCekInfoButtonClose(@FollowWidgetType widgetType: String, shopId: String,userId: String?,@MvcSource source: Int, buttonTitle:String?)

    //13,28
    fun viewTokomemberBottomSheet(shopId: String, userId: String?, @MvcSource source: Int)

    //14, 29
    fun clickDaftarJadiMember(shopId: String, userId: String?, @MvcSource source: Int)

    //15, 30
    fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int)

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43


    //MVC close memberhsip GTM
    fun clickLihatExpand(shopId: String, userId: String?, @MvcSource source: Int)

    fun  clickMulaiBelanjaButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?)

    //Reward GTM for Bottomsheet CTA
    fun userClickBottomSheetCTA(@FollowWidgetType widgetType: String, label: String, userId: String)

    fun tokomemberImpressionOnPdp(shopId: String,userId: String?, isTokomember: Boolean)

    fun viewMVCCoupon(label: String, mapData: HashMap< String,Any> , @MvcSource source: Int)

    fun mvcMultiShopCardClick(shopName: String, eventAction: String, @MvcSource source: Int, userId: String?, productPosition:Int, label: String = "")

}