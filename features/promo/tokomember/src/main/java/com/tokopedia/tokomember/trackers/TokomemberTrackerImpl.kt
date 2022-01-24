package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.FollowWidgetType

interface TokomemberTrackerImpl {

    fun userClickBottomSheetButton(@FollowWidgetType widgetType: String,shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int, isTokomember:Boolean)

    fun viewBottomSheetImpression(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int)

    fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int)

}