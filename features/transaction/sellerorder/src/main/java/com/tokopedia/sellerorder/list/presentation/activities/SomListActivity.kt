package com.tokopedia.sellerorder.list.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.fragments.SomContainerFragment
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.ActivitySomListBinding
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment

class SomListActivity : BaseActivity(), SomListLoadTimeMonitoringActivity {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SomListActivity::class.java)
    }

    private var _binding: ActivitySomListBinding? = null
    private val binding get() = _binding!!

    override var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring? = null
    override var loadTimeMonitoringListener: LoadTimeMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initSomListLoadTimeMonitoring()
        super.onCreate(savedInstanceState)
        _binding = ActivitySomListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        setupFragment()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is TkpdBaseV4Fragment) {
                if (it.onFragmentBackPressed()) return
            }
        }
        super.onBackPressed()
    }

    override fun initSomListLoadTimeMonitoring() {
        performanceMonitoringSomListPlt = SomListLoadTimeMonitoring()
        performanceMonitoringSomListPlt?.initPerformanceMonitoring()
    }

    override fun getSomListLoadTimeMonitoring() = performanceMonitoringSomListPlt

    private fun setupFragment() {
        inflateFragment()
    }

    private fun inflateFragment() {
        clearFragments()
        val newFragment = getNewFragment() ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, newFragment::class.java.simpleName)
                .commitNowAllowingStateLoss()
    }

    private fun clearFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            transaction.remove(fragment)
        }
        transaction.commitNowAllowingStateLoss()
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
        if (DeviceScreenInfo.isTablet(this)) {
            binding.toolbarShadow.show()
        } else {
            binding.toolbarShadow.gone()
        }
    }
}