package com.tokopedia.product.detail.view.viewholder.sdui

import android.annotation.SuppressLint
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp

class SDUICallback(
    private val mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<SDUIEvent>(mediator) {
    override fun onEvent(event: SDUIEvent) {
        when (event) {
            is SDUIEvent.SendTracker -> sendTracker(data = event)
        }
    }

    /**
     * This Function only Support EE Tracking.
     * We have remote config to switch between Tracking Queue and Direct Tracking.
     * Please REMOVE both remote config and Direct Tracking when PM says use "Grouping"
     */
    @SuppressLint("DeprecatedMethod")
    private fun sendTracker(data: SDUIEvent.SendTracker) {
        val useTrackingQueue = mediator.pdpRemoteConfig.getBoolean(
            RemoteConfigKey.ANDROID_PDP_ENABLE_SDUI_TRACKING_QUEUE,
            false
        )
        if (useTrackingQueue) mediator.queueTracker.putEETracking(data.eventMap)
        else TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data.eventMap)
    }
}
