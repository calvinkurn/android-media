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
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.track.TrackApp

const val KEY_INSTALL_REF_SHARED_PREF_FILE_NAME = "install_ref"
const val KEY_INSTALL_REF_INITIALISED = "install_ref_initialised"

class InstallReferral {

    private lateinit var referrerClient: InstallReferrerClient

    fun initilizeInstallReferral(context: Context) {
        referrerClient = InstallReferrerClient.newBuilder(context.applicationContext).build()
        val applicationContext = context.applicationContext
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        updateReferralCache()

                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl: String = response.installReferrer
                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
                        val appInstallTime: Long = response.installBeginTimestampSeconds
                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam

                        trackIfFromCampaignUrl(response.installReferrer)
                        sendToGA(context, response.installReferrer)
                        InstallUtils.sendIrisInstallEvent(context)
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        updateReferralCache()
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        updateReferralCache()
                    }
                    else -> {

                    }
                }
            }

            private fun updateReferralCache() {
                val localCacheHandler = LocalCacheHandler(applicationContext, KEY_INSTALL_REF_SHARED_PREF_FILE_NAME)
                val installRefInitialised = localCacheHandler.getBoolean(KEY_INSTALL_REF_INITIALISED)

                if (!installRefInitialised)
                    localCacheHandler.putBoolean(KEY_INSTALL_REF_INITIALISED, true)
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

    fun sendToGA(context: Context, referral: String) {
        val intent = Intent()
        Intent.ACTION_INSTALL_PACKAGE
        intent.action = InstallUtils.INSTALL_REFERRAL_ACTION
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
