package com.tokopedia.installreferral

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.analytics.CampaignTrackingReceiver
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.track.TrackApp
import java.util.HashMap

class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {

            trackIfFromCampaignUrl(intent.getStringExtra("referrer"))
            InstallUtils.sendIrisInstallEvent(context)
            CampaignTrackingReceiver().onReceive(context, intent)

        }

    }

    private fun trackIfFromCampaignUrl(referrer: String) {
        val uri = Uri.parse(referrer)
        if (uri != null && InstallUtils.isValidCampaignUrl(uri)) {
            val campaign = InstallUtils.splitquery(uri)
            TrackApp.getInstance().gtm.sendCampaign(campaign)
        }
    }

}