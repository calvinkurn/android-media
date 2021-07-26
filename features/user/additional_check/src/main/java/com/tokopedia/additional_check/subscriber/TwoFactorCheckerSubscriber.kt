package com.tokopedia.additional_check.subscriber

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.AdditionalCheckUseCaseModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_2FA
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_2FA_SELLER_APP
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 08/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TwoFactorCheckerSubscriber: Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var viewModel: TwoFactorViewModel

    var remoteConfig: FirebaseRemoteConfigImpl? = null

    private val exceptionPage = listOf(
            "ConsumerSplashScreen", "AddPinActivity", "AddPinFrom2FAActivity", "AddPhoneActivity", "TwoFactorActivity",
            "RegisterFingerprintOnboardingActivity", "VerificationActivity", "PinOnboardingActivity",
            "LogoutActivity", "LoginActivity","GiftBoxTapTapActivity", "GiftBoxDailyActivity", "RegisterInitialActivity",
            "RegisterEmailActivity", "AddNameRegisterPhoneActivity", "SmartLockActivity", "OvoRegisterInitialActivity", "OvoFinalPageActivity",
            "SettingProfileActivity"
    )

    private val exceptionPageSeller = listOf(
            "SplashScreenActivity", "AddPinActivity", "AddPinFrom2FAActivity", "AddPhoneActivity", "TwoFactorActivity",
            "RegisterFingerprintOnboardingActivity", "VerificationActivity", "PinOnboardingActivity",
            "LogoutActivity", "LoginActivity","GiftBoxTapTapActivity", "GiftBoxDailyActivity", "RegisterInitialActivity",
            "RegisterEmailActivity", "ChooseAccountActivity", "SmartLockActivity" , "ShopOpenRevampActivity" , "PinpointMapActivity",
            "SettingProfileActivity"
    )

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity != null) {
            if(!exceptionPage.contains(activity.javaClass.simpleName)) {
                DaggerAdditionalCheckComponents
                        .builder()
                        .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                        .additionalCheckModules(AdditionalCheckModules())
                        .additionalCheckUseCaseModules(AdditionalCheckUseCaseModules())
                        .build()
                        .inject(this)
                doChecking(activity)
            }
        }
    }

    private fun getTwoFactorRemoteConfig(activity: Activity?): Boolean? {
        if(remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(activity)
        }
        return remoteConfig?.getBoolean(REMOTE_CONFIG_2FA, false)
    }

    private fun getTwoFactorRemoteConfigSellerApp(): Boolean? {
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
        if(!exceptionPageSeller.contains(activity.javaClass.simpleName) && getTwoFactorRemoteConfigSellerApp() == true) {
            checking(activity)
        }
    }

    private fun checking(activity: Activity) {
        viewModel.check(onSuccess = {
            handleResponse(activity, twoFactorResult = it)
        }, onError = {
            it.printStackTrace()
        })
    }

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivityPaused(activity: Activity?) {}

    private fun handleResponse(activity: Activity?, twoFactorResult: TwoFactorResult){
        if(twoFactorResult.popupType == AdditionalCheckConstants.POPUP_TYPE_PHONE || twoFactorResult.popupType == AdditionalCheckConstants.POPUP_TYPE_PIN || twoFactorResult.popupType == AdditionalCheckConstants.POPUP_TYPE_BOTH){
            val i = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER).apply {
                putExtras(Bundle().apply {
                    putParcelable(TwoFactorFragment.RESULT_POJO_KEY, twoFactorResult)
                })
            }
            activity?.startActivity(i)
        }
    }

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityStopped(activity: Activity?) {}
}