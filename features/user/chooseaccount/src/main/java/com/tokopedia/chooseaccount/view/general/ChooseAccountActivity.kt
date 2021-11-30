package com.tokopedia.chooseaccount.view.general

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.common.analytics.LoginPhoneNumberAnalytics

/**
 * Created by Ade Fulki on 2020-01-23.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHOOSE_ACCOUNT
 */

open class ChooseAccountActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return ChooseAccountFragment.createInstance(bundle)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_choose_account
    }

    override fun getScreenName(): String? {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setWindowFlag(on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }
}