package com.tokopedia.report.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.ProductReportSubmitFragment

class ProductReportFormActivity : BaseSimpleActivity() {

    companion object{
        const val REASON_OBJECT = "reason"
        const val REASON_CACHE_ID = "reason_cache_id"
        fun createIntent(context: Context, reason: ProductReportReason)=
                Intent(context, ProductReportFormActivity::class.java).apply {
                    val cacheManager = SaveInstanceCacheManager(context, true).also {
                        it.put(REASON_OBJECT, reason)
                    }
                    putExtra(REASON_CACHE_ID, cacheManager.id)
                }
    }

    override fun getNewFragment(): Fragment {
        val cacheId = intent.getStringExtra(REASON_CACHE_ID)
        return ProductReportSubmitFragment.createInstance(cacheId)
    }
}