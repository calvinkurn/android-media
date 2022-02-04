package com.tokopedia.tokomember.trackers

import com.tokopedia.tokomember.util.MembershipWidgetType

open class DefaultTmTrackerImpl:TokomemberTrackerImpl {

    override fun userClickBottomSheetButton(@MembershipWidgetType widgetType: Int, shopId: String, paymentId:String, @TokomemberSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU

                when (widgetType) {
                    MembershipWidgetType.MEMBERSHIP_OPEN -> {
                        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TM_BS_OPEN_CTA
                    }
                    MembershipWidgetType.MEMBERSHIP_CLOSE -> {
                        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_TM_BS_CLOSE_CTA
                    }
                }
                map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"
            }
        }
        Tracker.fillCommonItems(map, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }


    override fun viewBottomSheetImpression(@MembershipWidgetType widgetType: Int, shopId: String, paymentId:String, @TokomemberSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.VIEW_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU
            }
        }
        when (widgetType) {
            MembershipWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_OPEN
            }
            MembershipWidgetType.MEMBERSHIP_CLOSE -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_CLOSE
            }
        }
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"
        Tracker.fillCommonItems(map, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun closeMainBottomSheet(@MembershipWidgetType widgetType: Int, shopId: String, paymentId:String, @TokomemberSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            TokomemberSource.THANK_YOU -> {
                map[Tracker.Constants.EVENT] = Tracker.Event.CLICK_TM_BOTTOM_SHEET
                map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_THANK_YOU
            }
        }
        when (widgetType) {
            MembershipWidgetType.MEMBERSHIP_OPEN -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_OPEN
            }
            MembershipWidgetType.MEMBERSHIP_CLOSE -> {
                map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_TM_BS_CLOSE
            }
        }
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $paymentId"

        Tracker.fillCommonItems(map, Tracker.Constants.TOKOPOINT_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

}