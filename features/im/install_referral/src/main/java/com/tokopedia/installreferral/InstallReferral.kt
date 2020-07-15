package com.tokopedia.installreferral

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.track.TrackApp

const val KEY_INSTALL_REF_SHARED_PREF_FILE_NAME = "install_ref"
const val KEY_INSTALL_REF_INITIALISED = "install_ref_initialised"
const val KEY_SCREEN_NAME = "screenName"
const val SCREEN_NAME = "install_referral"

class InstallReferral {

    private var referrerClient: InstallReferrerClient? = null

    fun initilizeInstallReferral(context: Context) {
        referrerClient = InstallReferrerClient.newBuilder(context.applicationContext).build()
        val applicationContext = context.applicationContext
        referrerClient?.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                try {
                    when (responseCode) {


                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            // Connection established.

                            referrerClient?.let {
                                val response: ReferrerDetails? = it.installReferrer

                                if (response != null) {
                                    response.installReferrer?.let { installReferrer ->
                                        trackIfFromCampaignUrl(installReferrer)
                                    }
                                }
                                InstallUtils.sendIrisInstallEvent(context)
                                updateReferralCache()

                            }
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
                } catch (e: Exception) {

                }
            }

            private fun updateReferralCache() {
                val localCacheHandler = LocalCacheHandler(applicationContext, KEY_INSTALL_REF_SHARED_PREF_FILE_NAME)
                val installRefInitialised = localCacheHandler.getBoolean(KEY_INSTALL_REF_INITIALISED)

                if (!installRefInitialised) {
                    localCacheHandler.putBoolean(KEY_INSTALL_REF_INITIALISED, true)
                    localCacheHandler.applyEditor()
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

    private fun trackIfFromCampaignUrl(referrer: String) {
        var uri = Uri.parse(referrer)
        val updatedReferrer: String
        if (!referrer.contains("?") && TextUtils.isEmpty(uri.host) && TextUtils.isEmpty(uri.scheme)) {
            updatedReferrer = "https://www.tokopedia.com/?$referrer"
            uri = Uri.parse(updatedReferrer)
        }

        if (uri != null && InstallUtils.isValidCampaignUrl(uri)) {
            val campaign: MutableMap<String, Any> = InstallUtils.splitquery(uri).toMutableMap()
            campaign[KEY_SCREEN_NAME] = SCREEN_NAME
            TrackApp.getInstance().gtm.sendCampaign(campaign)
        }
    }

}