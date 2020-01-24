package com.tokopedia.loginphone.chooseaccount.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.chooseaccount.view.fragment.ChooseAccountFragment
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics

/**
 * Created by Ade Fulki on 2020-01-23.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal#CHOOSE_ACCOUNT
 */

class ChooseAccountActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.WHITE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

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
}