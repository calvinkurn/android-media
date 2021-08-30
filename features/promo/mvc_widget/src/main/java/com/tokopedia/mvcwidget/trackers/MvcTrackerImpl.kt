package com.tokopedia.mvcwidget.trackers

import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.trackers.MvcSource

interface MvcTrackerImpl {
    //1 Pdp
    //16 Shop
    fun userClickEntryPoints(shopId: String, userId: String?, @MvcSource source: Int, isTokomember:Boolean)

    //3, 18
    fun clickFollowButton(shopId: String, userId: String?, @MvcSource source: Int)

    //4,10,19, 25
    fun viewFollowButtonToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean)

    //5,9,20,24,44,45 - DO NOT SEND TRACKERS YET - IT IS MUTUALLY DECIDED WITH PO
    fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int)

    //2,6,17,21
    fun viewWidgetImpression(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int)

    //7, 22
    fun clickJadiMemberButton(shopId: String, userId: String?, @MvcSource source: Int)

    //8,11,23,26
    fun viewJadiMemberToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean)

    //12, 27
    fun clickCekInfoButton(shopId: String, userId: String?, @MvcSource source: Int)

    fun clickCekInfoButtonClose(shopId: String,userId: String?,@MvcSource source: Int)

    //13,28
    fun viewTokomemberBottomSheet(shopId: String, userId: String?, @MvcSource source: Int)

    //14, 29
    fun clickDaftarJadiMember(shopId: String, userId: String?, @MvcSource source: Int)

    //15, 30
    fun closeMainBottomSheet(shopId: String, userId: String?, @MvcSource source: Int)

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43


    //MVC close memberhsip GTM
    fun clickLihatExpand(shopId: String, userId: String?, @MvcSource source: Int)

    fun  clickMulaiBelanjaButton(shopId: String, userId: String?, @MvcSource source: Int)

    //Reward GTM for Bottomsheet CTA
    fun userClickBottomSheetCTA(label: String, userId: String)

    fun tokomemberImpressionOnPdp(shopId: String,userId: String?)

}