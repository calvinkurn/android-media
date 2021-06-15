package com.tokopedia.sellerorder.list.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.fragments.SomContainerFragment
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.activity_som_list.*

class SomListActivity : BaseActivity(), SomListLoadTimeMonitoringActivity {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SomListActivity::class.java)
    }

    override var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring? = null
    override var loadTimeMonitoringListener: LoadTimeMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initSomListLoadTimeMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_som_list)
        setupStatusBar()
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        setupFragment(savedInstanceState)
    }

    override fun initSomListLoadTimeMonitoring() {
        performanceMonitoringSomListPlt = SomListLoadTimeMonitoring()
        performanceMonitoringSomListPlt?.initPerformanceMonitoring()
    }

    override fun getSomListLoadTimeMonitoring() = performanceMonitoringSomListPlt

    private fun setupFragment(savedInstance: Bundle?) {
        if (savedInstance == null) {
            inflateFragment()
        }
    }

    private fun inflateFragment() {
        val newFragment = getNewFragment() ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, newFragment::class.java.simpleName)
                .commit()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(SomConsts.TAB_ACTIVE, "")
        }
        intent?.data?.getQueryParameter(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID)?.let {
            bundle.putString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID, it)
        }
        return if (DeviceScreenInfo.isTablet(this)) {
            SomContainerFragment.newInstance(bundle)
        } else {
            SomListFragment.newInstance(bundle)
        }
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDarkMode()) {
                requestStatusBarLight()
            } else {
                requestStatusBarDark()
            }
            statusBarBackground?.show()
        }
        if (DeviceScreenInfo.isTablet(this)) {
            toolbarShadow?.show()
        } else {
            toolbarShadow?.gone()
        }
    }
}