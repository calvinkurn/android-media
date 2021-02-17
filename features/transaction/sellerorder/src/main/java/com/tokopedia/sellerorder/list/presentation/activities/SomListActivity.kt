package com.tokopedia.sellerorder.list.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment

class SomListActivity : BaseSomActivity(), SomListLoadTimeMonitoringActivity {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SomListActivity::class.java)
    }

    override var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring? = null
    override var loadTimeMonitoringListener: LoadTimeMonitoringListener? = null

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(SomConsts.TAB_ACTIVE, "")
        }
        return SomListFragment.newInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSomListLoadTimeMonitoring()
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    override fun initSomListLoadTimeMonitoring() {
        performanceMonitoringSomListPlt = SomListLoadTimeMonitoring()
        performanceMonitoringSomListPlt?.initPerformanceMonitoring()
    }

    override fun getSomListLoadTimeMonitoring() = performanceMonitoringSomListPlt
}