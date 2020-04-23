package com.tokopedia.vouchercreation.voucherlist.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_list)

        setupFragment()
        setWhiteStatusBar()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, VoucherListFragment.newInstance(true))
                .commitNowAllowingStateLoss()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }
}