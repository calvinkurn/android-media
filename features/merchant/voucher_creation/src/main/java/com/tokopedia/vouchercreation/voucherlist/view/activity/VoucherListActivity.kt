package com.tokopedia.vouchercreation.voucherlist.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListActivity : BaseActivity(), VoucherListFragment.Listener {

    companion object {
        const val SUCCESS_VOUCHER_ID_KEY = "success_voucher_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_list)

        showFragment(getFragment(true))
        setWhiteStatusBar()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment)
                .commitNowAllowingStateLoss()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun getFragment(isActiveVoucher: Boolean): VoucherListFragment {
        return VoucherListFragment.newInstance(isActiveVoucher).apply {
            setFragmentListener(this@VoucherListActivity)
        }
    }

    override fun switchFragment(isActiveVoucher: Boolean) {
        showFragment(getFragment(isActiveVoucher))
    }
}