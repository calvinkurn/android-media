package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.DetailedInvoiceAdapter
import com.tokopedia.thankyou_native.presentation.adapter.PurchaseDetailAdapter
import com.tokopedia.thankyou_native.presentation.viewModel.DetailInvoiceViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import javax.inject.Inject

class InvoiceFragment : BottomSheetUnify() {

    private lateinit var thanksPageData: ThanksPageData
    private var isV2: Boolean = false

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var invoiceAdapter: dagger.Lazy<DetailedInvoiceAdapter>

    @Inject
    lateinit var purchaseDetailAdapter: dagger.Lazy<PurchaseDetailAdapter>

    private val detailInvoiceViewModel: DetailInvoiceViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(DetailInvoiceViewModel::class.java)
    }


    private fun initInjector() {
        activity?.let {
            DaggerThankYouPageComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        } ?: run {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)!!
            }
            if (it.containsKey(ARG_ISV2)) {
                isV2 = it.getBoolean(ARG_ISV2)
            }
        }
        initInjector()
        initUI()
    }

    private fun initUI() {
        setTitle(getString(R.string.thank_payment_detail))
        val childView = LayoutInflater.from(context).inflate(R.layout.thank_payment_invoice_bsheet,
                null, false)
        setChild(childView)
        childView?.apply {
            initRecyclerView(findViewById(R.id.recyclerView))
            observeViewModel()
            if (isV2) {
                detailInvoiceViewModel.fetchPurchaseInfo(thanksPageData)
                return
            }
            detailInvoiceViewModel.createInvoiceData(thanksPageData)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetWrapper.setPadding(0, bottomSheetWrapper.paddingTop, 0, bottomSheetWrapper.paddingBottom)
        bottomSheetAction.setMargin(marginRight = 16.toPx())
        bottomSheetClose.setMargin(marginLeft = 16.toPx(), marginTop = 4.toPx(), marginRight = 12.toPx())
        isDragable = true
        isHideable = true
        customPeekHeight = getScreenHeight().toDp()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        if (isV2) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = purchaseDetailAdapter.get()
            return
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = invoiceAdapter.get()
    }

    private fun observeViewModel() {
        detailInvoiceViewModel.mutableInvoiceVisitables.observe(this, Observer {
            it?.let {
                addDataToAdapter(it)
            }
        })

        detailInvoiceViewModel.purchaseDetailVisitables.observe(this) {
            it?.let {
                addDataToAdapter(it)
            }
        }
    }

    private fun addDataToAdapter(list: ArrayList<Visitable<*>>) {
        if (isV2) {
            purchaseDetailAdapter.get().addItems(list)
            purchaseDetailAdapter.get().notifyItemRangeInserted(Int.ZERO, list.size - 1)
            return
        }
        invoiceAdapter.get().addItems(list)
        invoiceAdapter.get().notifyDataSetChanged()
    }

    companion object {
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        private const val TAG_INVOICE_BOTTOM_SHEET = "tag_invoice_bottom_sheet"
        private const val ARG_ISV2 = "arg_isv2"

        fun openInvoiceBottomSheet(activity: FragmentActivity?, thanksPageData: ThanksPageData) {
            activity?.apply {
                val invoiceBottomSheet = InvoiceFragment.getInvoiceFragment(thanksPageData, true)
                invoiceBottomSheet.show(supportFragmentManager, TAG_INVOICE_BOTTOM_SHEET)
            }
        }

        private fun getInvoiceFragment(thanksPageData: ThanksPageData, isV2: Boolean)
                : InvoiceFragment = InvoiceFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            bundle.putBoolean(ARG_ISV2, isV2)
            arguments = bundle
        }
    }
}


internal fun View.setMargin(marginLeft: Int = -1, marginTop: Int = -1, marginRight: Int = -1, marginBottom: Int = -1) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    val actualMarginLeft = if (marginLeft == -1) layoutParams.leftMargin else marginLeft
    val actualMarginTop = if (marginTop == -1) layoutParams.topMargin else marginTop
    val actualMarginRight = if (marginRight == -1) layoutParams.rightMargin else marginRight
    val actualMarginBottom = if (marginBottom == -1) layoutParams.bottomMargin else marginBottom

    layoutParams.setMargins(actualMarginLeft, actualMarginTop, actualMarginRight, actualMarginBottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = actualMarginLeft
        layoutParams.marginEnd = actualMarginRight
    }
}
