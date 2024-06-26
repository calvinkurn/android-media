package com.tokopedia.sellerorder.detail.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.detail.analytic.performance.SomDetailLoadTimeMonitoring
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailActivity : BaseSomActivity(), HasComponent<SomDetailComponent> {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, orderId: String) = Intent(context, SomDetailActivity::class.java)
                .putExtras(Bundle().apply {
                    putString(PARAM_ORDER_ID, orderId)
                })
    }

    var somDetailLoadTimeMonitoring: SomDetailLoadTimeMonitoring? = null
    var somLoadTimeMonitoringListener: LoadTimeMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initSOMDetailPlt()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            val orderId = intent?.data?.getQueryParameter(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
            bundle.putString(PARAM_ORDER_ID, orderId)
        }
        return SomDetailFragment.newInstance(bundle)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is TkpdBaseV4Fragment) {
                if (it.onFragmentBackPressed()) return
            }
        }
        val result = Intent().putExtra(SomConsts.RESULT_REFRESH_ORDER, (fragment as? SomDetailFragment)?.isDetailChanged ?: false)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun getComponent(): SomDetailComponent = DaggerSomDetailComponent.builder()
        .somComponent(SomComponentInstance.getSomComponent(application))
        .build()

    private fun initSOMDetailPlt() {
        somDetailLoadTimeMonitoring = SomDetailLoadTimeMonitoring()
        somDetailLoadTimeMonitoring?.initPerformanceMonitoring()
    }
}