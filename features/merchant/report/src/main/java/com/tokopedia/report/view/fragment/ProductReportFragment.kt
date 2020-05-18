package com.tokopedia.report.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_report.*
import javax.inject.Inject


class ProductReportFragment : BaseDaggerFragment(), ReportReasonAdapter.OnReasonClick {

    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private var productId = "-1"
    private val tracking by lazy { MerchantReportTracking() }

    override fun scrollToTop() {}

    override fun gotoForm(reason: ProductReportReason) {
        tracking.eventReportReason(reason.strLabel)
        activity?.let {
            startActivityForResult(ProductReportFormActivity.createIntent(it, reason, productId), REQUEST_CODE_FORM_SUBMIT)
        }
    }

    val isInRoot: Boolean
        get() = adapter.filteredId.isEmpty()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductReportViewModel
    private val adapter: ReportReasonAdapter by lazy { ReportReasonAdapter(this, tracking) }

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
                    Toaster.make(findViewById(android.R.id.content), ErrorHandler.getErrorMessage(this,
                            it.throwable), Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                            getString(com.tokopedia.abstraction.R.string.close))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_FORM_SUBMIT){
            activity?.run {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        viewModel.reasonResponse.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    fun onBackPressed() {
        adapter.back()
    }

    companion object{
        private const val ARG_PRODUCT_ID = "arg_product_id"
        private const val REQUEST_CODE_FORM_SUBMIT = 100

        fun createInstance(productId: String) = ProductReportFragment().apply {
            arguments = Bundle().also {
                it.putString(ARG_PRODUCT_ID, productId)
            }
        }
    }
}