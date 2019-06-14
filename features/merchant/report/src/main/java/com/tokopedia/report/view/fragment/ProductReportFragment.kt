package com.tokopedia.report.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.ToasterError
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_report.*
import javax.inject.Inject
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import com.tokopedia.report.view.activity.ProductReportFormActivity


class ProductReportFragment : BaseDaggerFragment(), ReportReasonAdapter.OnReasonClick {

    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private var productId = "-1"

    override fun scrollToTop() {
        //smoothScroller.targetPosition = 0
    }

    override fun gotoForm(reason: ProductReportReason) {
        activity?.let {
            startActivityForResult(ProductReportFormActivity.createIntent(it, reason, productId), 100)
        }
    }

    val isInRoot: Boolean
        get() = adapter.filteredId.isEmpty()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductReportViewModel
    private val adapter: ReportReasonAdapter by lazy { ReportReasonAdapter(this) }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(MerchantReportComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val vmProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = vmProvider.get(ProductReportViewModel::class.java)
        }
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID, "-1")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.reasonResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetReason(it.data)
                is Fail -> activity?.run {
                    ToasterError.showClose(this, ErrorHandler.getErrorMessage(this,
                            it.throwable))
                }
            }
        })
    }

    private fun onSuccessGetReason(reasons: List<ProductReportReason>) {
        adapter.changeList(reasons)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = adapter
        recycler_view.clearItemDecoration()
    }

    override fun onDestroy() {
        viewModel.reasonResponse.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    fun onBackPressed() {
        adapter.back()
    }

    companion object{
        private const val ARG_PRODUCT_ID = "arg_product_id"

        fun createInstance(productId: String) = ProductReportFragment().apply {
            arguments = Bundle().also {
                it.putString(ARG_PRODUCT_ID, productId)
            }
        }
    }
}