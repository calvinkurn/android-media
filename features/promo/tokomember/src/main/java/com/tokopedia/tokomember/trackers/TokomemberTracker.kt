package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.FollowWidgetType

class TokomemberTracker{
    var trackerImpl:TokomemberTrackerImpl = DefaultTmTrackerImpl()

    fun userClickBottomSheetButton(@FollowWidgetType widgetType: String,shopId: String, paymentId: String, userId: String?, @TokomemberSource source: Int, isTokomember:Boolean){
        trackerImpl.userClickBottomSheetButton(widgetType,shopId,paymentId,userId,source,isTokomember)
    }

    fun viewBottomSheetImpression(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int){
        trackerImpl.viewBottomSheetImpression(widgetType,shopId,paymentId,userId, source)
    }

    fun closeMainBottomSheet(@FollowWidgetType widgetType: String,shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int) {
        trackerImpl.closeMainBottomSheet(widgetType,shopId,paymentId,userId, source)
    }

}