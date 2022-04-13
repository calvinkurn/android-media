package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.MembershipWidgetType

class TokomemberTracker {
    var trackerImpl:TokomemberTrackerImpl = DefaultTmTrackerImpl()

    fun userClickBottomSheetButton(@MembershipWidgetType widgetType: Int, paymentId: String, @TokomemberSource source: Int){
        trackerImpl.userClickBottomSheetButton(widgetType,paymentId,source)
    }

    fun viewBottomSheetImpression(@MembershipWidgetType widgetType: Int, paymentId:String, @TokomemberSource source: Int){
        trackerImpl.viewBottomSheetImpression(widgetType,paymentId, source)
    }

    fun closeMainBottomSheet(@MembershipWidgetType widgetType: Int, paymentId:String, @TokomemberSource source: Int) {
        trackerImpl.closeMainBottomSheet(widgetType,paymentId, source)
    }

}