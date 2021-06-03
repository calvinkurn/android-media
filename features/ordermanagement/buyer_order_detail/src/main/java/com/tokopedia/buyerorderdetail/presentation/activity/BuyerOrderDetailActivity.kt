package com.tokopedia.buyerorderdetail.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.performance.BuyerOrderDetailLoadMonitoring
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailModule
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment

class BuyerOrderDetailActivity: BaseSimpleActivity(), HasComponent<BuyerOrderDetailComponent> {

    var buyerOrderDetailLoadMonitoring: BuyerOrderDetailLoadMonitoring? = null

    override fun getNewFragment(): Fragment? {
        return BuyerOrderDetailFragment.newInstance(requireNotNull(intent.extras))
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        return DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .buyerOrderDetailModule(BuyerOrderDetailModule())
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            initLoadMonitoring()
        }
        super.onCreate(savedInstanceState)
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
        setupWindowColor()
    }

    private fun setupWindowColor() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, R.color.buyer_order_detail_window_color))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_right_in_medium, com.tokopedia.resources.common.R.anim.slide_left_out_medium)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_right_in_medium, com.tokopedia.resources.common.R.anim.slide_left_out_medium)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_right_in_medium, com.tokopedia.resources.common.R.anim.slide_left_out_medium)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initLoadMonitoring() {
        buyerOrderDetailLoadMonitoring = BuyerOrderDetailLoadMonitoring()
        buyerOrderDetailLoadMonitoring?.initPerformanceMonitoring()
    }
}