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
import com.tokopedia.additional_check.view.BottomSheetCheckViewModel
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 08/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class TwoFactorCheckerSubscriber: Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var viewModel: BottomSheetCheckViewModel

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        DaggerAdditionalCheckComponents
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .additionalCheckModules(AdditionalCheckModules(activity))
                .additionalCheckUseCaseModules(AdditionalCheckUseCaseModules())
                .build()
                .inject(this)

        if(!activity.javaClass.simpleName.toString().contains("ConsumerSplashScreen") &&
                !activity.javaClass.simpleName.toString().contains("AddPinActivity") &&
                !activity.javaClass.simpleName.toString().contains("AddPhoneActivity") &&
                !activity.javaClass.simpleName.toString().contains("TwoFactorActivity") &&
                !activity.javaClass.simpleName.toString().contains("RegisterFingerprintOnboardingActivity") &&
                !activity.javaClass.simpleName.toString().contains("VerificationActivity") &&
                !activity.javaClass.simpleName.toString().contains("PinOnboardingActivity")&&
                !activity.javaClass.simpleName.toString().contains("LogoutActivity")
                ) {
            viewModel.check(onSuccess = {
                handleResponse(activity, twoFactorResult = it)
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    private fun handleResponse(activity: Activity?, twoFactorResult: TwoFactorResult){
        if(twoFactorResult.popupType == AdditionalCheckConstants.POPUP_TYPE_PHONE || twoFactorResult.popupType == AdditionalCheckConstants.POPUP_TYPE_PIN){
            val i = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER).apply {
                putExtras(Bundle().apply {
                    putParcelable(TwoFactorFragment.RESULT_POJO_KEY, twoFactorResult)
                })
            }
            activity?.startActivity(i)
        }
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityStopped(activity: Activity?) {
    }
}