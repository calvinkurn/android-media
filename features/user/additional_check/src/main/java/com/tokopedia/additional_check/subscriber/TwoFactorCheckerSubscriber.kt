package com.tokopedia.additional_check.subscriber

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.additional_check.data.OfferingData
import com.tokopedia.additional_check.data.ShowInterruptData
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_NONE
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PHONE
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PIN
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_2FA
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_2FA_SELLER_APP
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 08/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TwoFactorCheckerSubscriber: Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var viewModel: TwoFactorViewModel

    private var remoteConfig: FirebaseRemoteConfigImpl? = null

    private val exceptionPage = listOf(
            "ConsumerSplashScreen", "AddPinActivity", "AddPinFrom2FAActivity", "AddPhoneActivity", "TwoFactorActivity",
            "RegisterFingerprintOnboardingActivity", "VerificationActivity", "PinOnboardingActivity",
            "LogoutActivity", "LoginActivity","GiftBoxTapTapActivity", "GiftBoxDailyActivity", "RegisterInitialActivity",
            "RegisterEmailActivity", "AddNameRegisterPhoneActivity", "SmartLockActivity", "OvoRegisterInitialActivity", "OvoFinalPageActivity",
            "SettingProfileActivity", "LinkAccountReminderActivity", "SilentVerificationActivity", "LinkAccountWebViewActivity", "BiometricOfferingActivity",
            "RegisterFingerprintActivity", "VerifyFingerprintActivity"
    )

    private val exceptionPageSeller = listOf(
            "SplashScreenActivity", "AddPinActivity", "AddPinFrom2FAActivity", "AddPhoneActivity", "TwoFactorActivity",
            "RegisterFingerprintOnboardingActivity", "VerificationActivity", "PinOnboardingActivity",
            "LogoutActivity", "LoginActivity","GiftBoxTapTapActivity", "GiftBoxDailyActivity", "RegisterInitialActivity",
            "RegisterEmailActivity", "ChooseAccountActivity", "SmartLockActivity" , "ShopOpenRevampActivity" , "PinpointMapActivity",
            "SettingProfileActivity", "LinkAccountReminderActivity", "SilentVerificationActivity", "LinkAccountWebViewActivity", "BiometricOfferingActivity",
            "RegisterFingerprintActivity", "VerifyFingerprintActivity"
    )


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if(!exceptionPage.contains(activity.javaClass.simpleName)) {
            DaggerAdditionalCheckComponents
                    .builder()
                    .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
                    .additionalCheckModules(AdditionalCheckModules())
                    .build()
                    .inject(this)

            doChecking(activity)
        }
    }

    private fun getTwoFactorRemoteConfig(activity: Activity?): Boolean? {
        if(remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(activity)
        }
        return remoteConfig?.getBoolean(REMOTE_CONFIG_2FA, false)
    }

    private fun getTwoFactorRemoteConfigSellerApp(activity: Activity): Boolean? {
        if(remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(activity)
        }
        return remoteConfig?.getBoolean(REMOTE_CONFIG_2FA_SELLER_APP, false)
    }

    private fun doChecking(activity: Activity){
        if (GlobalConfig.isSellerApp()) {
            checkSellerApp(activity)
        } else {
            checkMainApp(activity)
        }
    }

    private fun checkMainApp(activity: Activity) {
        if(!exceptionPage.contains(activity.javaClass.simpleName) && getTwoFactorRemoteConfig(activity) == true) {
            checking(activity)
        }
    }

    private fun checkSellerApp(activity: Activity) {
        if(!exceptionPageSeller.contains(activity.javaClass.simpleName) && getTwoFactorRemoteConfigSellerApp(activity) == true) {
            checking(activity)
        }
    }

    private fun checking(activity: Activity) {
//        viewModel.check(onSuccess = {
//            handleResponse(activity, showInterruptData = it)
//        }, onError = {
//            it.printStackTrace()
//        })
        viewModel.getOffering(BiometricPromptHelper.isBiometricAvailableActivity(activity), {
            handleResponseOfferingData(activity, it)
        }, {
            it.printStackTrace()
        })
    }

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}


    private fun handleResponseOfferingData(activity: Activity?, offeringList: MutableList<OfferingData>){
//        val newOfferingList = mutableListOf(
//            OfferingData(name = "pin", false),
//            OfferingData(name = "biometric", false),
//        )

        if(offeringList.isNotEmpty()) {
            val firstIntent = mapToApplink(activity!!, offeringList.first(), true)
            if(firstIntent != null) {
                activity.startActivity(firstIntent)
            }
        }
    }

    private fun handleResponse(activity: Activity?, showInterruptData: ShowInterruptData){
        if(showInterruptData.error.isEmpty()) {
            val result = TwoFactorResult(
                showSkipButton = showInterruptData.showSkipButton,
                popupType = showInterruptData.popupType
            )

            if (result.popupType == POPUP_TYPE_NONE &&
                showInterruptData.accountLinkReminderData.showReminder &&
                !isOtherPopupShowing(activity) &&
                !GlobalConfig.isSellerApp()) {
                    gotoLinkAccountReminder(activity)
            } else if (result.popupType == AdditionalCheckConstants.POPUP_TYPE_PHONE ||
                    result.popupType == AdditionalCheckConstants.POPUP_TYPE_PIN ||
                    result.popupType == AdditionalCheckConstants.POPUP_TYPE_BOTH
            ) {
                goTo2FAPage(activity, result)
            }
        }
    }

    private fun isOtherPopupShowing(mActivity: Activity?): Boolean {
        return try {
            if (mActivity != null) {
                CMInAppManager.getInstance().externalInAppCallback?.isInAppViewVisible(mActivity) ?: true
            } else {
                true
            }
        } catch (e: Exception) {
            true
        }
    }
    private fun gotoLinkAccountReminder(mActivity: Activity?) {
        if(whiteListedPageAccountLinkReminder.contains(mActivity?.javaClass?.simpleName)) {
            val intent = RouteManager.getIntent(mActivity, ApplinkConstInternalGlobal.LINK_ACC_REMINDER)
            mActivity?.startActivity(intent)
        }
    }

    private fun goTo2FAPage(activity: Activity?, twoFactorResult: TwoFactorResult) {
        val i = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER)
            .apply {
                putExtras(Bundle().apply {
                    putParcelable(TwoFactorFragment.RESULT_POJO_KEY, twoFactorResult)
                })
            }
        activity?.startActivity(i)
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    companion object {
        fun mapToApplink(activity: Activity, offer: OfferingData, isFirst: Boolean = false): Intent? {
            val result = TwoFactorResult(
                showSkipButton = offer.enableSkip,
                popupType = POPUP_TYPE_NONE
            )

            val intent: Intent? = when(offer.name) {
                "pin" -> {
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER)
                    result.popupType = POPUP_TYPE_PIN
                    intent
                }
                "phone" -> {
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER)
                    result.popupType = POPUP_TYPE_PHONE
                    intent
                }
                "accounts-link" -> {
                    if(isFirst) {
                        if(whiteListedPageAccountLinkReminder.contains(activity.javaClass.simpleName)) {
                            return RouteManager.getIntent(
                                activity,
                                ApplinkConstInternalGlobal.LINK_ACC_REMINDER
                            )
                        }
                    }
                    return RouteManager.getIntent(
                        activity,
                        ApplinkConstInternalGlobal.LINK_ACC_REMINDER
                    )
                }
                "biometric" -> {
                    if(isFirst) {
                        if(whiteListedPageBiometricOffering.contains(activity.javaClass.simpleName)) {
                            return RouteManager.getIntent(
                                activity,
                                ApplinkConstInternalUserPlatform.BIOMETRIC_OFFERING
                            )
                        }
                    }
                    return RouteManager.getIntent(
                        activity,
                        ApplinkConstInternalUserPlatform.BIOMETRIC_OFFERING
                    )
                }
                else -> null
            }
            intent?.apply {
                putExtras(Bundle().apply {
                    putBoolean(TwoFactorFragment.IS_FROM_2FA, true)
                    putParcelable(TwoFactorFragment.RESULT_POJO_KEY, result)
                })
            }
            return intent
        }

        fun mapStringToOfferData(additionalCheckPreference: AdditionalCheckPreference, activity: Activity, offerData: String): Intent? {
            return try {
                val offerData = Gson().fromJson(offerData, OfferingData::class.java)
                additionalCheckPreference.clearNextOffer()
                mapToApplink(activity, offerData)
            }catch (e: Exception){
                null
            }
        }

        // Account linking reminder bottom sheet only showing in this page
        private val whiteListedPageAccountLinkReminder = listOf(
            "MainParentActivity", "DeveloperOptionActivity"
        )

        // Biometric offering bottom sheet only showing in this page
        private val whiteListedPageBiometricOffering = listOf(
            "MainParentActivity", "DeveloperOptionActivity"
        )

    }
}