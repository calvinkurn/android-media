package com.tokopedia.vouchercreation.voucherlist.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout(savedInstanceState)
        setupView()
        setWhiteStatusBar()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    override fun getNewFragment(): Fragment? = VoucherListFragment.newInstance(true)
}