package com.tokopedia.pms.paymentlist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.VaTransactionItem
import com.tokopedia.pms.paymentlist.presentation.adapter.PaymentTransactionDetailAdapter
import com.tokopedia.pms.paymentlist.presentation.listeners.PaymentListActionListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.pms_base_recycler_bottom_sheet.*

class PaymentTransactionDetailSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.pms_base_recycler_bottom_sheet
    private var vaTransactionList: ArrayList<VaTransactionItem> = arrayListOf()
    private var sheetTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            vaTransactionList = it.getParcelableArrayList(TRANSACTION_LIST) ?: arrayListOf()
            sheetTitle = "Transaksi ${it.getString(GATEWAY_NAME) ?: ""}"
        }
    }

    private fun setDefaultParams() {
        setTitle(sheetTitle)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        baseRecyclerView.adapter = PaymentTransactionDetailAdapter(vaTransactionList) { action, trxName, model ->
            when(action) {
                OPEN_DETAIL -> showInvoiceDetail(model)
                CANCEL_TRANSACTION -> cancelTransaction(model, trxName)
            }
        }
        baseRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun showInvoiceDetail(model: VaTransactionItem) {
        RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, model.invoiceUrl)
    }

    private fun cancelTransaction(model: VaTransactionItem, transactionName: String?) {
        (activity as PaymentListActionListener).cancelSingleTransaction(model.transactionId, model.merchantCode, transactionName)
        dismiss()
    }

    companion object {
        const val TRANSACTION_LIST = "TransactionList"
        const val GATEWAY_NAME = "GatewayName"

        const val OPEN_DETAIL = 1
        const val CANCEL_TRANSACTION = 2

        private const val TAG = "PaymentTransactionActionSheet"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val fragment = PaymentTransactionDetailSheet().apply {
                arguments = bundle
            }
            fragment.show(childFragmentManager, TAG)
        }
    }
}