package com.tokopedia.pms.paymentlist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.presentation.adapter.PaymentTransactionActionAdapter
import com.tokopedia.pms.paymentlist.presentation.listeners.PaymentListActionListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.pms_base_recycler_bottom_sheet.*

class PaymentTransactionActionSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.pms_base_recycler_bottom_sheet
    private var actionList: ArrayList<TransactionActionType> = arrayListOf()
    private lateinit var model: BasePaymentModel
    private val sheetTitle: String = "Lainnya"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            model = it.getParcelable(PAYMENT_MODEL)!!
            actionList.addAll(model.actionList)
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
        baseRecyclerView.adapter = PaymentTransactionActionAdapter(actionList) { actionModel ->
            parseAction(actionModel)
        }
        baseRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun parseAction(actionModel: TransactionActionType) {
        activity?.let {
            (it as PaymentListActionListener).let { listener ->
                when (actionModel) {
                    is CancelTransaction -> handleCancelPendingPayment(listener)
                    is EditKlicBCA -> listener.changeBcaUserId(model)
                    is UploadProof -> listener.uploadPaymentProof(model)
                    is EditBankTransfer -> listener.changeAccountDetail(model)
                }
            }
        }
        dismiss()
    }

    private fun handleCancelPendingPayment(listener: PaymentListActionListener) {
        when (model) {
            is VirtualAccountPaymentModel -> {
                (model as VirtualAccountPaymentModel).let {
                    if (it.transactionList.size > 1) listener.cancelCombinedTransaction(model)
                    else listener.cancelSingleTransaction(
                        it.transactionList.getOrNull(0)?.transactionId ?: "",
                        it.transactionList.getOrNull(0)?.merchantCode ?: "",
                        null
                    )
                }
            }
            else -> listener.cancelSingleTransaction(
                model.extractValues().first,
                model.extractValues().second, null
            )
        }
    }

    companion object {
        const val PAYMENT_MODEL = "PaymentModel"
        private const val TAG = "PaymentTransactionActionSheet"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val fragment = PaymentTransactionActionSheet().apply {
                arguments = bundle
            }
            fragment.show(childFragmentManager, TAG)
        }
    }
}