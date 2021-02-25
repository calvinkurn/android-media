package com.tokopedia.notifcenter.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateSellerFragment

/**
 * Created by faisalramd on 05/02/20.
 */

class NotificationSellerActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeWhiteIfSellerApp()
        super.onCreate(savedInstanceState)

        setWhiteStatusBarIfSellerApp()
    }

    override fun getNewFragment(): Fragment {
        return NotificationUpdateSellerFragment()
    }

    private fun setThemeWhiteIfSellerApp() {
        if (GlobalConfig.isSellerApp()) {
            setTheme(com.tokopedia.abstraction.R.style.Theme_WhiteUnify)
        }
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, NotificationSellerActivity::class.java)
        }
    }
}