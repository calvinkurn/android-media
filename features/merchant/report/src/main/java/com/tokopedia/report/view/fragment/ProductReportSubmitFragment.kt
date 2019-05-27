package com.tokopedia.report.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.adapter.ReportFormAdapter
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import kotlinx.android.synthetic.main.fragment_product_report.*

class ProductReportSubmitFragment : BaseDaggerFragment() {
    private lateinit var adapter: ReportFormAdapter
    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cacheId = arguments?.getString(ProductReportFormActivity.REASON_CACHE_ID, "") ?: ""
        val reason: ProductReportReason? = context?.let {
            val cacheManager = SaveInstanceCacheManager(it, cacheId)
            cacheManager.get(ProductReportFormActivity.REASON_OBJECT, ProductReportReason::class.java)

        }
        recycler_view.clearItemDecoration()
        reason?.let {
            adapter = ReportFormAdapter(it)
            recycler_view.adapter = adapter
        }
    }

    companion object {

        fun createInstance(cacheId: String) = ProductReportSubmitFragment().apply {
            arguments = Bundle().also {
                it.putString(ProductReportFormActivity.REASON_CACHE_ID, cacheId)
            }
        }
    }
}