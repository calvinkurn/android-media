package com.tokopedia.additional_check.subscriber

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.AdditionalCheckUseCaseModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
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

//    private var isShown = false

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

    }

    override fun onActivityDestroyed(activity: Activity?) {
//        isShown = false
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    private fun handleResponse(activity: Activity?, twoFactorResult: TwoFactorResult){
        if(!twoFactorResult.isHavePin || !twoFactorResult.isHavePhone){
            val i = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.TWO_FACTOR_REGISTER).apply {
                putExtras(Bundle().apply {
                    putParcelable(TwoFactorFragment.RESULT_POJO_KEY, twoFactorResult)
                })
            }
            activity?.startActivity(i)
//            isShown = true
        }
    }


    override fun onActivityResumed(activity: Activity?) {
//        if(!isShown) {
            viewModel.check(onSuccess = {
                handleResponse(activity, twoFactorResult = it)
            }, onError = {
                it.printStackTrace()
            })
//        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }
}