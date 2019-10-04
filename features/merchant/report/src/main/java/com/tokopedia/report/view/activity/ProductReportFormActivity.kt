package com.tokopedia.report.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.fragment.ProductReportSubmitFragment

class ProductReportFormActivity : BaseSimpleActivity(), HasComponent<MerchantReportComponent> {
    override fun getComponent(): MerchantReportComponent = DaggerMerchantReportComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object{
        const val REASON_OBJECT = "reason"
        const val REASON_CACHE_ID = "reason_cache_id"
        const val PRODUCT_ID = "product_id"
        fun createIntent(context: Context, reason: ProductReportReason, productId: String)=
                Intent(context, ProductReportFormActivity::class.java).apply {
                    val cacheManager = SaveInstanceCacheManager(context, true).also {
                        it.put(REASON_OBJECT, reason)
                    }
                    putExtra(REASON_CACHE_ID, cacheManager.id)
                    putExtra(PRODUCT_ID, productId)
                }
    }

    override fun getNewFragment(): Fragment {
        val cacheId = intent.getStringExtra(REASON_CACHE_ID)
        val productId = intent.getStringExtra(PRODUCT_ID)
        return ProductReportSubmitFragment.createInstance(cacheId, productId)
    }
}