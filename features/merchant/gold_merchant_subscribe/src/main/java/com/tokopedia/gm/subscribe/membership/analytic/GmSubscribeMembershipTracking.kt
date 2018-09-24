package com.tokopedia.gm.subscribe.membership.analytic

import com.tokopedia.gm.subscribe.GmSubscribeModuleRouter
import java.util.HashMap

class GmSubscribeMembershipTracking (private val gmSubscribeModuleRouter: GmSubscribeModuleRouter){

    private fun eventGmMembershipSubscribe(action: String) {
        val eventMap = createEventMap(GmSubscribeMembershipTrackingConstant.ACTION_GM_SUBSCRIBE_AUTO_EXTEND, GmSubscribeMembershipTrackingConstant.CATEGORY_GM_SUBSCRIBE_AUTO_EXTEND, action, "")
        gmSubscribeModuleRouter.sendEventTrackingGmSubscribe(eventMap)
    }

    private fun createEventMap(event: String, category: String, action: String, label: String): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[GmSubscribeMembershipTrackingConstant.EVENT] = event
        eventMap[GmSubscribeMembershipTrackingConstant.EVENT_CATEGORY] = category
        eventMap[GmSubscribeMembershipTrackingConstant.EVENT_ACTION] = action
        eventMap[GmSubscribeMembershipTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    fun eventClickToggleAutoExtend(action: String) {
        eventGmMembershipSubscribe(GmSubscribeMembershipTrackingConstant.CLICK_TOGGLE + action)
    }

    fun eventClickExtend() {
        eventGmMembershipSubscribe(GmSubscribeMembershipTrackingConstant.CLICK_EXTEND)
    }

    fun eventClickSubscribe(subscription_type: String) {
        eventGmMembershipSubscribe(GmSubscribeMembershipTrackingConstant.CLICK_SUBSCRIBE + subscription_type)
    }

    fun eventClickInformation() {
        eventGmMembershipSubscribe(GmSubscribeMembershipTrackingConstant.CLICK_LEARN_MORE)
    }
}