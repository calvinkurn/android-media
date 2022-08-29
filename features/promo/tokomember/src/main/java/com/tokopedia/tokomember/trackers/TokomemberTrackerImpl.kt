package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.MembershipWidgetType

interface TokomemberTrackerImpl {

    fun userClickBottomSheetButton(@MembershipWidgetType widgetType: Int, paymentId:String, @TokomemberSource source: Int)

    fun viewBottomSheetImpression(@MembershipWidgetType widgetType: Int, paymentId:String, @TokomemberSource source: Int)

    fun closeMainBottomSheet(@MembershipWidgetType widgetType: Int, paymentId:String, @TokomemberSource source: Int)

}