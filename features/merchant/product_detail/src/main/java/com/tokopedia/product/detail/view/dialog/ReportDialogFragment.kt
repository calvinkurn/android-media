package com.tokopedia.product.detail.view.dialog

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.viewmodel.ProductReportViewModel
import javax.inject.Inject

class ReportDialogFragment : DialogFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productReportViewModel: ProductReportViewModel
    lateinit var productId: String

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
        productReportViewModel.getProductReportType()
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