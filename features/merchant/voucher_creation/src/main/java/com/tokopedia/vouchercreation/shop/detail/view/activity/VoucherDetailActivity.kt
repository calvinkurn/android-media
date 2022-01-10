package com.tokopedia.vouchercreation.shop.detail.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.getIntIntentExtra
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoring
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringInterface
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringListener
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringType
import com.tokopedia.vouchercreation.shop.detail.view.fragment.VoucherDetailFragment

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailActivity : BaseActivity(), MvcPerformanceMonitoringListener {

    companion object {
        const val VOUCHER_ID = "voucher_id"

        fun createDetailIntent(context: Context): Intent {
            return Intent(context, VoucherDetailActivity::class.java)
        }
    }

    private val performanceMonitoring: MvcPerformanceMonitoringInterface by lazy {
        MvcPerformanceMonitoring(MvcPerformanceMonitoringType.Detail)
    }

    private val voucherId by getIntIntentExtra(VOUCHER_ID, 0)

    override fun getScreenName(): String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceMonitoring.initMvcPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_detail)

        window.decorView.setBackgroundColor(Color.WHITE)
        showFragment()
        setWhiteStatusBar()
    }

    override fun startNetworkPerformanceMonitoring() {
        performanceMonitoring.startNetworkMvcPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        performanceMonitoring.startRenderMvcPerformanceMonitoring()
    }

    override fun finishMonitoring() {
        performanceMonitoring.stopPerformanceMonitoring()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.voucherDetailContainer, getPageFragment())
                .commitNowAllowingStateLoss()
    }

    private fun getPageFragment(): Fragment = VoucherDetailFragment.newInstance(voucherId)

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.setBackgroundColor(Color.WHITE)
            setStatusBarColor(Color.TRANSPARENT)
            setLightStatusBar(true)
        }
    }
}