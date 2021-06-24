package com.tokopedia.tokopedianow.home.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.fragment.HomeFragment

class HomeActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_home)
        setStatusBarColor()
        setBackgroundColor()
        attachFragment()
    }

    private fun attachFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
    }

    private fun setBackgroundColor() {
        window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}