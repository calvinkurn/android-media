package com.tokopedia.additional_check.view.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.additional_check.common.ADD_PHONE_NUMBER_PAGE
import com.tokopedia.additional_check.common.ADD_PIN_PAGE
import com.tokopedia.additional_check.common.ActivePageListener
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_DOUBLE_TAP
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class TwoFactorActivity: BaseSimpleActivity(), ActivePageListener {
    private var doubleTapExit = false

    var enableBackBtn: Boolean? = true
    var remoteConfig: FirebaseRemoteConfigImpl? = null

    private val twoFactorTracker = TwoFactorTracker()
    private var currentPage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        remoteConfig = FirebaseRemoteConfigImpl(this)
        enableBackBtn = intent?.extras?.getParcelable<TwoFactorResult>(TwoFactorFragment.RESULT_POJO_KEY)?.showSkipButton
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
    }

    fun onFragmentCreated(){
        fragment?.run {
            if(this is TwoFactorFragment){
                this.setActiveListener(this@TwoFactorActivity)
            }
        }
    }

    override fun getNewFragment(): Fragment? {
        return TwoFactorFragment.newInstance(intent?.extras)
    }

    override fun onBackPressed() {
        when (currentPage) {
            ADD_PHONE_NUMBER_PAGE -> { }
            ADD_PIN_PAGE -> { twoFactorTracker.clickCloseButtonPageAddPin() }
        }

        if(enableBackBtn == true) {
            super.onBackPressed()
        }else {
            if(remoteConfig?.getBoolean(REMOTE_CONFIG_DOUBLE_TAP, false) == false)
                doubleTapExit()
            else super.onBackPressed()
        }
    }

    private fun doubleTapExit() {
        if (doubleTapExit) {
            finishAffinity()
        } else {
            doubleTapExit = true
            val exitMessage = "Tekan sekali lagi untuk keluar"
            Toast.makeText(this, exitMessage, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleTapExit = false }, 2000L)
        }
    }

    override fun currentPage(page: String) {
        currentPage = page
    }
}