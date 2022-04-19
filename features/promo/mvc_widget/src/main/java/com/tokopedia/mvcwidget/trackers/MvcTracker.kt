package com.tokopedia.mvcwidget.trackers

import com.tokopedia.mvcwidget.FollowWidgetType

class MvcTracker{
    var trackerImpl:MvcTrackerImpl = DefaultMvcTrackerImpl()
    
    //1 Pdp
    //16 Shop
    fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        @MvcSource source: Int,
        isTokomember: Boolean,
        productId: String = ""
    ){
        trackerImpl.userClickEntryPoints(shopId,userId,source,isTokomember, productId)
    }

    fun userClickEntryPointOnMVCLockToProduct(shopId: String, userId: String?, @MvcSource source: Int, productId: String){
        trackerImpl.userClickEntryPointOnMVCLockToProduct(shopId,userId,source, productId)
    }

    fun viewMVCLockToProduct(shopId: String, userId: String?, @MvcSource source: Int, productId: String){
        trackerImpl.viewMVCLockToProduct(shopId,userId,source, productId)
    }

    //3, 18
    fun clickFollowButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        trackerImpl.clickFollowButton(widgetType,shopId,userId,source,buttonTitle)
    }

    //4,10,19, 25
    fun viewFollowButtonToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean) {
        trackerImpl.viewFollowButtonToast(shopId,userId,source, isSuccess)
    }

    //5,9,20,24,44,45 - DO NOT SEND TRACKERS YET - IT IS MUTUALLY DECIDED WITH PO
    fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int){
        trackerImpl.viewCoupons(widgetType,shopId,userId, source)
    }

    //2,6,17,21
    fun viewWidgetImpression(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int){
        trackerImpl.viewWidgetImpression(widgetType,shopId,userId, source)
    }

    //7, 22
    fun clickJadiMemberButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        trackerImpl.clickJadiMemberButton(widgetType,shopId,userId, source, buttonTitle)
    }

    //8,11,23,26
    fun viewJadiMemberToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean){
        trackerImpl.viewJadiMemberToast(shopId,userId, source, isSuccess)
    }

    //12, 27
    fun clickCekInfoButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?) {
        trackerImpl.clickCekInfoButton(widgetType,shopId,userId, source,buttonTitle)
    }

    fun clickCekInfoButtonClose(@FollowWidgetType widgetType: String, shopId: String,userId: String?,@MvcSource source: Int, buttonTitle:String?){
        trackerImpl.clickCekInfoButtonClose(widgetType,shopId,userId, source,buttonTitle)
    }

    //13,28
    fun viewTokomemberBottomSheet(shopId: String, userId: String?, @MvcSource source: Int){
        trackerImpl.viewTokomemberBottomSheet(shopId,userId, source)
    }

    //14, 29
    fun clickDaftarJadiMember(shopId: String, userId: String?, @MvcSource source: Int) {
        trackerImpl.clickDaftarJadiMember(shopId,userId, source)
    }

    //15, 30
    fun closeMainBottomSheet(@FollowWidgetType widgetType: String,shopId: String, userId: String?, @MvcSource source: Int) {
        trackerImpl.closeMainBottomSheet(widgetType,shopId,userId, source)
    }

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43


    //MVC close memberhsip GTM
    fun clickLihatExpand(shopId: String, userId: String?, @MvcSource source: Int){
        trackerImpl.clickLihatExpand(shopId,userId, source)
    }

    fun  clickMulaiBelanjaButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int, buttonTitle:String?){
        trackerImpl.clickMulaiBelanjaButton(widgetType,shopId,userId, source,buttonTitle)
    }

    //Reward GTM for Bottomsheet CTA
    fun userClickBottomSheetCTA(@FollowWidgetType widgetType: String, label: String, userId: String) {
        trackerImpl.userClickBottomSheetCTA(widgetType,label,userId)
    }

    fun tokomemberImpressionOnPdp(shopId: String,userId: String?, isTokomember: Boolean){
        trackerImpl.tokomemberImpressionOnPdp(shopId,userId, isTokomember)
    }

    //Multishop
    fun viewMVCCoupon(label: String, mapData: HashMap< String,Any> , @MvcSource source: Int){
        trackerImpl.viewMVCCoupon(label,mapData,source)
    }

    fun mvcMultiShopCardClick(shopName: String, eventAction: String, @MvcSource source: Int, userId: String?, productPosition:Int, label: String = ""){
        trackerImpl.mvcMultiShopCardClick(shopName,eventAction,source,userId,productPosition,label)
    }
}