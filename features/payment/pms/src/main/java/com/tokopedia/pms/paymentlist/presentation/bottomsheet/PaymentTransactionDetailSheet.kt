package com.tokopedia.pms.paymentlist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.analytics.PmsEvents
import com.tokopedia.pms.databinding.PmsBaseRecyclerBottomSheetBinding
import com.tokopedia.pms.paymentlist.domain.data.VaTransactionItem
import com.tokopedia.pms.paymentlist.presentation.adapter.PaymentTransactionDetailAdapter
import com.tokopedia.pms.paymentlist.presentation.listener.PaymentListActionListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PaymentTransactionDetailSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<PmsBaseRecyclerBottomSheetBinding>()
    private var vaTransactionList: ArrayList<VaTransactionItem> = arrayListOf()
    private var sheetTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
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
        customPeekHeight = getScreenHeight().toDp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PmsBaseRecyclerBottomSheetBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        binding?.run {
            baseRecyclerView.adapter =
                PaymentTransactionDetailAdapter(vaTransactionList) { action, trxName, model ->
                    when (action) {
                        OPEN_DETAIL -> showInvoiceDetail(model)
                        CANCEL_TRANSACTION -> cancelTransaction(model, trxName)
                    }
                }
            baseRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showInvoiceDetail(model: VaTransactionItem) {
        (activity as PaymentListActionListener).showInvoiceDetail(model.invoiceUrl)
    }

    private fun cancelTransaction(model: VaTransactionItem, transactionName: String?) {
        (activity as PaymentListActionListener).cancelSingleTransaction(
            model.transactionId,
            model.merchantCode,
            transactionName,
            PmsEvents.InvokeCancelTransactionOnDetailEvent(5)
        )
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
