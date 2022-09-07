package com.tokopedia.report.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.fragment.ProductReportComposeFragment

class ProductReportActivity: BaseSimpleActivity(), HasComponent<MerchantReportComponent> {
    private lateinit var fragment : ProductReportComposeFragment

    override fun getNewFragment(): Fragment {
        val productId = intent.data?.lastPathSegment?.let {
            it
        } ?: (intent.extras?.getString(ARG_PRODUCT_ID) ?: "-1")
        fragment = ProductReportComposeFragment.createInstance(productId)
        return fragment
    }

    override fun getComponent(): MerchantReportComponent = DaggerMerchantReportComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object {
        private const val ARG_PRODUCT_ID = "arg_product_id"
        fun getCallingIntent(context: Context, productId: String): Intent =
                Intent(context, ProductReportActivity::class.java).putExtra(ARG_PRODUCT_ID, productId)
    }

    override fun onBackPressed() {
        if(::fragment.isInitialized && !fragment.isInRoot){
            fragment.onBackPressed()
        } else
            super.onBackPressed()
    }
}