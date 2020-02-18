package com.tokopedia.installreferral

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.android.gms.analytics.CampaignTrackingReceiver
import com.tokopedia.track.TrackApp

class InstallReferral {

    private lateinit var referrerClient: InstallReferrerClient

    fun initilizeInstallReferral(context: Context) {
        referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.

                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl: String = response.installReferrer
                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
                        val appInstallTime: Long = response.installBeginTimestampSeconds
                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam

                        Toast.makeText(context, response.toString() + " and " + response.installReferrer, Toast.LENGTH_LONG).show()
                        Log.d("install_referral","from api"+ response.toString() + " and " + response.installReferrer)
                        trackIfFromCampaignUrl(response.installReferrer)
                        sendToGA(context, response.installReferrer)
                        InstallUtils.sendIrisInstallEvent(context)
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                    }
                    else -> {

                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {

            }
        })
    }

    fun sendToGA(context: Context, referral: String) {
        val intent = Intent()
        intent.action="com.android.vending.INSTALL_REFERRER"
        intent.putExtra("referrer", referral)
        CampaignTrackingReceiver().onReceive(context, intent)

    }

    private fun trackIfFromCampaignUrl(referrer: String) {
        val uri = Uri.parse(referrer)
        if (uri != null && InstallUtils.isValidCampaignUrl(uri)) {
            val campaign = InstallUtils.splitquery(uri)
            TrackApp.getInstance().gtm.sendCampaign(campaign)
        }
    }

}
