package com.tokopedia.product.detail.view.dialog

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.ToasterError
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.reporttype.ReportType
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.ReportTypeAdapter
import com.tokopedia.product.detail.view.viewmodel.ProductReportViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_dialog_report_product.*
import javax.inject.Inject

class ReportDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productReportViewModel: ProductReportViewModel
    lateinit var productId: String
    var reportTypeList: List<ReportType> = listOf()

    companion object {
        val TAG = ReportDialogFragment::class.java.simpleName
        const val ARG_PRODUCT_ID = "product_id"

        fun newInstance(productId: String): ReportDialogFragment {
            return ReportDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
        (activity as HasComponent<ProductDetailComponent>).component.inject(this)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        productReportViewModel = viewModelProvider.get(ProductReportViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.run {
            requestFeature(Window.FEATURE_NO_TITLE)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        return inflater.inflate(R.layout.fragment_dialog_report_product, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productReportViewModel.getProductReportType(productId)
        cancel_but.setOnClickListener { dismiss() }
        report.setOnClickListener {
            //TODO

        }
    }

    private fun onSuccessGetReportType(reportTypeList: List<ReportType>) {
        this.reportTypeList = reportTypeList
        reportSpinner.adapter = ReportTypeAdapter(activity,
                android.R.layout.simple_list_item_1,
                reportTypeList.mapNotNull { it.reportTitle })
    }

    @SuppressLint("Range")
    private fun onErrorGetReportType(throwable: Throwable) {
        activity?.run {
            ToasterError.make(findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(context, throwable))
                    .show()
        }
        dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productReportViewModel.productReportTypeResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetReportType(it.data)
                is Fail -> onErrorGetReportType(it.throwable)
            }
        })
    }

    override fun onResume() {
        dialog.window?.run {
            val params = dialog.window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params as WindowManager.LayoutParams
        }
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        productReportViewModel.productReportTypeResp.removeObservers(this)
        productReportViewModel.clear()
    }

    override fun show(manager: FragmentManager, tag: String) {
        val fragment = manager.findFragmentByTag(tag)
        if (fragment != null) {
            val ft = manager.beginTransaction()
            ft.remove(fragment)
            ft.commit()
        }
        super.show(manager, tag)
    }
}