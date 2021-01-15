package com.tokopedia.additional_check.view.activity

import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.REMOTE_CONFIG_DOUBLE_TAP
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl


/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class TwoFactorActivity: BaseSimpleActivity() {
    private var doubleTapExit = false

    var enableBackBtn: Boolean? = true
    var remoteConfig: FirebaseRemoteConfigImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remoteConfig = FirebaseRemoteConfigImpl(this)
        enableBackBtn = intent?.extras?.getParcelable<TwoFactorResult>(TwoFactorFragment.RESULT_POJO_KEY)?.showSkipButton
        val title = SpannableString(getString(R.string.two_factor_title))
        title.setSpan(TextAppearanceSpan(this, R.style.TitleAppearance), 0, title.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        toolbar.title = title
    }

    override fun getNewFragment(): Fragment? {
        return TwoFactorFragment.newInstance(intent?.extras)
    }

    override fun onBackPressed() {
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

}