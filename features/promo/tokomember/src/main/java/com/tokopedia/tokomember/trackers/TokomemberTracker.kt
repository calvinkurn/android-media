package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.MembershipWidgetType

class TokomemberTracker {
    var trackerImpl:TokomemberTrackerImpl = DefaultTmTrackerImpl()

    fun userClickBottomSheetButton(@MembershipWidgetType widgetType: Int,shopId: String, paymentId: String, @TokomemberSource source: Int){
        trackerImpl.userClickBottomSheetButton(widgetType,shopId,paymentId,source)
    }

    fun viewBottomSheetImpression(@MembershipWidgetType widgetType: Int, shopId: String, paymentId:String, @TokomemberSource source: Int){
        trackerImpl.viewBottomSheetImpression(widgetType,shopId,paymentId, source)
    }

    fun closeMainBottomSheet(@MembershipWidgetType widgetType: Int,shopId: String, paymentId:String, @TokomemberSource source: Int) {
        trackerImpl.closeMainBottomSheet(widgetType,shopId,paymentId, source)
    }

}