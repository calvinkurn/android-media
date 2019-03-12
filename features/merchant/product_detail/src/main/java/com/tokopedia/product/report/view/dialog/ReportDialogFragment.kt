package com.tokopedia.product.report.view.dialog

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.AdapterView
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.report.model.reportType.ReportType
import com.tokopedia.product.report.view.adapter.ReportTypeAdapter
import com.tokopedia.product.report.view.tracking.ProductReportTracking
import com.tokopedia.product.report.view.viewmodel.ProductReportViewModel
import kotlinx.android.synthetic.main.fragment_dialog_report_product.*
import javax.inject.Inject

class ReportDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productReportViewModel: ProductReportViewModel
    lateinit var productId: String
    var reportTypeList: List<ReportType> = listOf()

    private val productReportTracking: ProductReportTracking by lazy {
        ProductReportTracking((context?.applicationContext as? AbstractionRouter)?.analyticTracker)
    }

    companion object {
        val TAG = ReportDialogFragment::class.java.simpleName
        const val ARG_PRODUCT_ID = "product_id"
        const val CONTACT_US_URL = "https://www.tokopedia.com/contact-us.pl"

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
        btnReport.isEnabled = false
        productReportViewModel.getProductReportType(productId,
                onSuccessGetReportType = this::onSuccessGetReportType,
                onErrorGetReportType = this::onErrorGetReportType)
        btnCancel.setOnClickListener { dismiss() }
        btnReport.setOnClickListener {
            val description = etDesc.text.toString().trim()
            if (description.isEmpty()) {
                tilDescription.error = getString(R.string.description_must_be_filled)
                return@setOnClickListener
            }
            productReportViewModel.reportProduct(productId,
                    reportTypeList[reportSpinner.selectedItemPosition].reportId.toString(),
                    description,
                    onSuccessReportProduct = this::onSuccessReportProduct,
                    onErrorReportProduct = this::onErrorReportProduct)
            btnReport.isEnabled = false
        }

        etDesc.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                tilDescription.error = null
            }
        })
        reportSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                val selectedReport = reportTypeList[position]
                if (selectedReport.useRedirectButton) {
                    tilDescription.visibility = View.GONE
                    btnReport.visibility = View.GONE
                    tvRedirect.visibility = View.VISIBLE
                    setRedirect(reportTypeList[position].reportTitle, reportTypeList[position].reportUrl
                            ?: "")
                    btnCancel.text = getString(R.string.label_close)
                } else {
                    tilDescription.visibility = View.VISIBLE
                    btnReport.visibility = View.VISIBLE
                    tvRedirect.visibility = View.GONE
                    btnCancel.text = getString(R.string.label_cancel)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    private fun setRedirect(item: String?, link: String) {
        var status = false
        val string = resources.getString(R.string.redirect_report_product)

        val caseReplace = getString(R.string.label_case).toLowerCase()
        val stringNoResult = SpannableString(string.replace(caseReplace, item!!))

        val linkStatus = getString(R.string.this_link).toLowerCase()
        if (link == CONTACT_US_URL) {
            status = true
        }

        stringNoResult.setSpan(redirect(status, link),
                stringNoResult.toString().indexOf(linkStatus),
                stringNoResult.toString().indexOf(linkStatus) + linkStatus.length, 0)

        tvRedirect.run {
            movementMethod = LinkMovementMethod.getInstance()
            text = stringNoResult
        }
    }

    private fun redirect(status: Boolean, link: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.run {
                    val intent = (applicationContext as ApplinkRouter).getApplinkIntent(this,
                            ApplinkConst.CONTACT_US)
                    if (intent != null) {
                        intent.putExtra("PARAM_REDIRECT", status)
                        intent.putExtra("PARAM_URL", link)
                        startActivity(intent)
                    }
                }

            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(activity!!, R.color.tkpd_main_green)
            }
        }
    }

    private fun onSuccessGetReportType(reportTypeList: List<ReportType>) {
        context?.run {
            this@ReportDialogFragment.reportTypeList = reportTypeList
            reportSpinner.adapter = ReportTypeAdapter(this,
                android.R.layout.simple_list_item_1,
                reportTypeList.mapNotNull { it.reportTitle })
            btnReport.isEnabled = true
        }
    }

    @SuppressLint("Range")
    private fun onErrorGetReportType(throwable: Throwable) {
        activity?.run {
            ToasterError.make(findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(context, throwable),
                    ToasterError.LENGTH_LONG)
                    .show()
        }
        dismiss()
    }

    private fun onSuccessReportProduct() {
        btnReport.isEnabled = true
        productReportTracking.eventSubmitReport()
        activity?.run {
            ToasterNormal.make(findViewById(android.R.id.content),
                    getString(R.string.thanks_for_product_report),
                    ToasterNormal.LENGTH_LONG)
                    .show()
        }
        dismiss()
    }

    @SuppressLint("Range")
    private fun onErrorReportProduct(throwable: Throwable) {
        activity?.run {
            ToasterError.make(findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(context, throwable),
                    ToasterError.LENGTH_LONG)
                    .show()
        }
        dismiss()
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