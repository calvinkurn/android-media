package com.tokopedia.vouchercreation.voucherlist.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoring
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringInterface
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringListener
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringType
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListActivity : BaseActivity(),
        VoucherListFragment.Listener, MvcPerformanceMonitoringListener {

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

    private val voucherListPerformanceMonitoring: MvcPerformanceMonitoringInterface by lazy {
        MvcPerformanceMonitoring(MvcPerformanceMonitoringType.List)
    }

    private var isSuccessDialogAlreadyShowed = false

    private var isActiveVoucher = true

    private val successVoucherId by lazy { intent?.extras?.getInt(SUCCESS_VOUCHER_ID_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherListPerformanceMonitoring.initMvcPerformanceMonitoring()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_list)

        window.decorView.setBackgroundColor(Color.WHITE)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        var isActive = true
        val isUpdateVoucherSuccess = intent?.getBooleanExtra(UPDATE_VOUCHER_KEY, false) ?: false
        val voucherId = intent?.extras?.getInt(SUCCESS_VOUCHER_ID_KEY)
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

        showFragment(getFragment(isActive, isUpdateVoucherSuccess, voucherId))
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

    override fun startNetworkPerformanceMonitoring() {
        voucherListPerformanceMonitoring.startNetworkMvcPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        voucherListPerformanceMonitoring.startRenderMvcPerformanceMonitoring()
    }

    override fun finishMonitoring() {
        voucherListPerformanceMonitoring.stopPerformanceMonitoring()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment)
                .commitNowAllowingStateLoss()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(Color.TRANSPARENT)
                setLightStatusBar(true)
            } catch (ex: Resources.NotFoundException) {
                Timber.e(ex)
            }
        }
    }

    private fun getFragment(isActiveVoucher: Boolean,
                            isUpdateVoucherSuccess: Boolean = false,
                            voucherId: Int? = successVoucherId): VoucherListFragment {
        return VoucherListFragment.newInstance().apply {
            setFragmentListener(this@VoucherListActivity)
            val bundle = Bundle().apply {
                putBoolean(VoucherListFragment.KEY_IS_ACTIVE_VOUCHER, isActiveVoucher)
            }
            val willShowSuccessCreationDialog = !isSuccessDialogAlreadyShowed && isActiveVoucher && voucherId != 0
            if (willShowSuccessCreationDialog) {
                with(bundle) {
                    putBoolean(VoucherListFragment.IS_SUCCESS_VOUCHER, true)
                    voucherId?.let { putInt(VoucherListFragment.VOUCHER_ID_KEY, it) }
                }
                isSuccessDialogAlreadyShowed = true
            } else {
                if (isUpdateVoucherSuccess) {
                    bundle.putBoolean(VoucherListFragment.IS_UPDATE_VOUCHER, true)
                }
            }
            arguments = bundle
        }
    }
}