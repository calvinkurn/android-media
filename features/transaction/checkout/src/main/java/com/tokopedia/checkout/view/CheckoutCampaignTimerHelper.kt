package com.tokopedia.checkout.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.view.dialog.ExpiredTimeDialog.Companion.newInstance
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.unifycomponents.TimerUnify
import com.tokopedia.utils.time.TimeHelper.timeBetweenRFC3339
import javax.inject.Inject

// Need to create kotlin helper class since need to pass function for widget TimerUnifyHighlight and ShipmentFragment is still in Java

class CheckoutCampaignTimerHelper @Inject constructor() {
    private var fragment: ShipmentFragment? = null
    private var tracker: CheckoutAnalyticsCourierSelection? = null
    private var timerData: CampaignTimerUi? = null

    fun setCampaignTimer(shipmentFragment: ShipmentFragment,
                         shipmentTracker: CheckoutAnalyticsCourierSelection,
                         timerView: TimerUnify,
                         campaignTimerData: CampaignTimerUi?) {
        if (campaignTimerData != null && campaignTimerData.showTimer) {
            fragment = shipmentFragment
            tracker = shipmentTracker
            timerData = campaignTimerData
            val diff = timeBetweenRFC3339(campaignTimerData.timerServer, campaignTimerData.timerExpired)
            timerView.remainingMilliseconds = diff
            timerView.onFinish = ::onFinish
        }
    }

    private fun onFinish() {
        fragment?.let { fragment ->
            if (fragment.isResumed) {
                fragment.fragmentManager?.let { fragmentManager ->
                    timerData?.let { timerData ->
                        tracker?.let { tracker ->
                            val dialog = newInstance(timerData, tracker, fragment)
                            dialog.show(fragmentManager, "expired dialog")
                        }
                    }
                }
            }
        }
    }

}