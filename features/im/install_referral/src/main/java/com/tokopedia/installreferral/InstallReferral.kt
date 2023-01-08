package com.tokopedia.installreferral

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.android.gms.analytics.CampaignTrackingReceiver
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.track.TrackApp
import timber.log.Timber
import java.lang.Exception

const val KEY_INSTALL_REF_SHARED_PREF_FILE_NAME = "install_ref"
const val KEY_INSTALL_REF_INITIALISED = "install_ref_initialised"
const val KEY_SCREEN_NAME = "screenName"
const val SCREEN_NAME = "install_referral"

class InstallReferral {

    private var referrerClient: InstallReferrerClient? = null
    private var installReferrerInterface: InstallReferrerInterface? = null

    fun initilizeInstallReferral(context: Context) {
        referrerClient = InstallReferrerClient.newBuilder(context.applicationContext).build()
        val applicationContext = context.applicationContext
        try {
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
                                            sendToGA(applicationContext, installReferrer)
                                            executeInstallReferrerCallback(installReferrer)
                                        }
                                    }
                                    InstallUtils.sendIrisInstallEvent(applicationContext)
                                    updateReferralCache()
                                    executeInstallReferrerCallback("")
                                }
                            }
                            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                // API not available on the current Play Store app.
                                updateReferralCache()
                                executeInstallReferrerCallback("")
                            }
                            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                                // Connection couldn't be established.
                                updateReferralCache()
                                executeInstallReferrerCallback("")
                            }
                            else -> {
                                executeInstallReferrerCallback("")
                            }
                        }
                    } catch (e: Exception) {
                        executeInstallReferrerCallback("")
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
                    executeInstallReferrerCallback("")
                }
            })
        }catch (ex:SecurityException) {
            Timber.d(ex)
        }
    }

    private fun executeInstallReferrerCallback(installReferrer:String?){
        installReferrerInterface?.installReferrerDataRetrived(installReferrer)
        installReferrerInterface = null
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
            var campaign: MutableMap<String, Any> = InstallUtils.splitquery(uri).toMutableMap()
            campaign[KEY_SCREEN_NAME] = SCREEN_NAME
            TrackApp.getInstance().gtm.sendCampaign(campaign)
        }
    }

    fun setInstallReferrerInterface(installReferrerInterface: InstallReferrerInterface?){
        this.installReferrerInterface = installReferrerInterface
    }

}