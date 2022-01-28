package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.FollowWidgetType

open class DefaultTmTrackerImpl:TokomemberTrackerImpl {

    override fun userClickBottomSheetButton(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int, isTokomember:Boolean) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU

                when (widgetType) {
                    FollowWidgetType.MEMBERSHIP_OPEN -> {
                        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TM_BS_OPEN_CTA
                    }
                    FollowWidgetType.MEMBERSHIP_CLOSE -> {
                        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TM_BS_CLOSE_CTA
                    }
                }
                map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"
            }
        }
        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }


    override fun viewBottomSheetImpression(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU
            }
        }
        when (widgetType) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_OPEN
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_CLOSE
            }
        }
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"
        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, paymentId:String, userId: String?, @TokomemberSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU
            }
        }
        when (widgetType) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_OPEN
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_CLOSE
            }
        }
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"

        Tracker.fillCommonItems(map, userId, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

}