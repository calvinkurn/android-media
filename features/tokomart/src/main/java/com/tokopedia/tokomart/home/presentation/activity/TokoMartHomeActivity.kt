package com.tokopedia.tokomart.home.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment

class TokoMartHomeActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomart_home)
        setStatusBarColor()
        attachFragment()
    }

    private fun attachFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, TokoMartHomeFragment.newInstance())
                .commit()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
    }
}