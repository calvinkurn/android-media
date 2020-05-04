package com.tokopedia.vouchercreation.detail.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.view.fragment.VoucherDetailFragment

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailActivity : BaseActivity() {

    override fun getScreenName(): String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_detail)

        showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.voucherDetailContainer, VoucherDetailFragment.newInstance())
                .commitNowAllowingStateLoss()
    }
}