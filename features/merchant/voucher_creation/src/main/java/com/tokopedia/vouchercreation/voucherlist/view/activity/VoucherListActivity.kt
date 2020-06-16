package com.tokopedia.vouchercreation.voucherlist.view.activity

import android.content.Context
import android.content.Intent
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
        @JvmStatic
        fun createInstance(context: Context,
                           isActive: Boolean): Intent =
                Intent(context, VoucherListActivity::class.java).apply {
                    putExtra(IS_ACTIVE, isActive)
                }

        const val SUCCESS_VOUCHER_ID_KEY = "success_voucher_id"
        const val UPDATE_VOUCHER_KEY = "update_voucher"

        private const val IS_ACTIVE = "is_active"

        const val ACTIVE = "active"
        const val HISTORY = "history"
    }

    private var isSuccessDialogAlreadyShowed = false

    private var isActiveVoucher = true

    private val successVoucherId by lazy { intent?.extras?.getInt(SUCCESS_VOUCHER_ID_KEY) }
    private val isUpdateVoucherSuccess by lazy { intent?.extras?.getBoolean(UPDATE_VOUCHER_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_list)

        setWhiteStatusBar()

        var isActive = true

        intent?.extras?.getBoolean(IS_ACTIVE, true)?.let {
            isActive = it
        }

        intent?.data?.lastPathSegment?.let { status ->
            if (status.isNotEmpty()) {
                isActive =
                        when(status) {
                            ACTIVE -> true
                            HISTORY -> false
                            else -> true
                        }
            }
        }

        showFragment(getFragment(isActive))
    }

    override fun onBackPressed() {
        if (isActiveVoucher) {
            super.onBackPressed()
        } else {
            switchFragment(true)
        }
    }

    override fun switchFragment(isActiveVoucher: Boolean) {
        this.isActiveVoucher = isActiveVoucher
        showFragment(getFragment(isActiveVoucher))
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
            val willShowSuccessCreationDialog = !isSuccessDialogAlreadyShowed && isActiveVoucher && successVoucherId != 0
            if (willShowSuccessCreationDialog) {
                val bundle = Bundle().apply bun@{
                    isUpdateVoucherSuccess?.run {
                        putBoolean(VoucherListFragment.IS_UPDATE_VOUCHER, true)
                        return@bun
                    }
                    successVoucherId?.run {
                        putBoolean(VoucherListFragment.IS_SUCCESS_VOUCHER, true)
                        putInt(VoucherListFragment.VOUCHER_ID_KEY, this)
                    }
                }
                isSuccessDialogAlreadyShowed = true
                arguments = bundle
            }
        }
    }
}