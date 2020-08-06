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
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
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
            "ConsumerSplashScreen", "AddPinActivity", "AddPhoneActivity", "TwoFactorActivity",
            "RegisterFingerprintOnboardingActivity", "VerificationActivity", "PinOnboardingActivity",
            "LogoutActivity", "LoginActivity","GiftBoxTapTapActivity", "GiftBoxDailyActivity"
    )

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        DaggerAdditionalCheckComponents
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .additionalCheckModules(AdditionalCheckModules(activity))
                .additionalCheckUseCaseModules(AdditionalCheckUseCaseModules())
                .build()
                .inject(this)
        remoteConfig = FirebaseRemoteConfigImpl(activity)
        doChecking(activity)
    }

    private fun getTwoFactorRemoteConfig(): Boolean? {
        return remoteConfig?.getBoolean(REMOTE_CONFIG_2FA, false)
    }

    private fun doChecking(activity: Activity){
        if(!exceptionPage.contains(activity.javaClass.simpleName.toString()) && getTwoFactorRemoteConfig() == true) {
            viewModel.check(onSuccess = {
                handleResponse(activity, twoFactorResult = it)
            }, onError = {
                it.printStackTrace()
            })
        }
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