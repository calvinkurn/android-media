package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.databinding.BottomsheetSecondaryBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker

class SomBottomSheetProductEmpty(
    context: Context
) : SomBaseRejectOrderBottomSheet<BottomsheetSecondaryBinding>(context, LAYOUT, SomConsts.TITLE_PILIH_PRODUK_KOSONG), SomBottomSheetStockEmptyAdapter.ProductCheckChangedListener {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    private var rejectReason: SomReasonRejectData.Data.SomRejectReason? = null
    private var orderId: String = ""
    private var listener: SomRejectOrderBottomSheetListener? = null
    private var somBottomSheetStockEmptyAdapter: SomBottomSheetStockEmptyAdapter = SomBottomSheetStockEmptyAdapter(this)

    override fun bind(view: View): BottomsheetSecondaryBinding {
        return BottomsheetSecondaryBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            rvBottomsheetSecondary.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetStockEmptyAdapter
            }

            setupTicker()
            tfExtraNotes.show()
            tfExtraNotes.setLabelStatic(true)
            tfExtraNotes.textFiedlLabelText.text = context.getString(R.string.empty_stock_extra_note)
            tfExtraNotes.setPlaceholder(context.getString(R.string.empty_stock_extra_placeholder))

            btnPrimary.show()
            btnPrimary.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam()
                orderRejectRequest.orderId = orderId
                orderRejectRequest.rCode = rejectReason?.reasonCode?.orZero().toString()
                var strListPrd = ""
                var indexPrd = 0
                somBottomSheetStockEmptyAdapter.getListProductEmptied().forEach {
                    if (indexPrd > 0) strListPrd += "~"
                    strListPrd += it.id
                    indexPrd++
                }
                orderRejectRequest.listPrd = strListPrd
                orderRejectRequest.reason = tfExtraNotes.textFieldInput.text.toString()
                listener?.onDoRejectOrder(orderRejectRequest)
            }
            hideKeyboardHandler.attachListener(btnPrimary)
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun onProductCheckChanged() {
        binding?.run {
            btnPrimary.isEnabled = somBottomSheetStockEmptyAdapter.getListProductEmptied().isNotEmpty()
            root.hideKeyboard()
        }
    }

    private fun reset() {
        binding?.run {
            somBottomSheetStockEmptyAdapter.reset()
            tfExtraNotes.textFieldInput.setText("")
            btnPrimary.isEnabled = false
        }
    }

    private fun setupTicker() {
        binding?.run {
            if (!rejectReason?.reasonTicker.isNullOrEmpty()) {
                tickerPenaltySecondary.show()
                tickerPenaltySecondary.tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerPenaltySecondary.setHtmlDescription(rejectReason?.reasonTicker.orEmpty())
            } else {
                tickerPenaltySecondary.gone()
            }
        }
    }

    fun setProducts(listProduct: List<SomDetailOrder.GetSomDetail.Details.Product>) {
        somBottomSheetStockEmptyAdapter.listProduct = listProduct.toMutableList()
        somBottomSheetStockEmptyAdapter.notifyDataSetChanged()
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setRejectReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        this.rejectReason = rejectReason
        setupTicker()
    }

    fun setListener(listener: SomRejectOrderBottomSheetListener) {
        this.listener = listener
    }
}
