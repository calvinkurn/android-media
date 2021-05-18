package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomBottomSheetProductEmpty(
        context: Context,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet(context, LAYOUT, SomConsts.TITLE_PILIH_PRODUK_KOSONG), SomBottomSheetStockEmptyAdapter.ProductCheckChangedListener {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    private var somBottomSheetStockEmptyAdapter: SomBottomSheetStockEmptyAdapter = SomBottomSheetStockEmptyAdapter(this)

    override fun setupChildView() {
        childViews?.run {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetStockEmptyAdapter
            }

            setupTicker()
            tf_extra_notes?.show()
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = context.getString(R.string.empty_stock_extra_note)
            tf_extra_notes?.setPlaceholder(context.getString(R.string.empty_stock_extra_placeholder))

            fl_btn_primary?.show()
            fl_btn_primary?.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam()
                orderRejectRequest.orderId = orderId
                orderRejectRequest.rCode = rejectReason.reasonCode.toString()
                var strListPrd = ""
                var indexPrd = 0
                somBottomSheetStockEmptyAdapter.getListProductEmptied().forEach {
                    if (indexPrd > 0) strListPrd += "~"
                    strListPrd += it.id
                    indexPrd++
                }
                orderRejectRequest.listPrd = strListPrd
                orderRejectRequest.reason = tf_extra_notes?.textFieldInput?.text.toString()
                listener.onDoRejectOrder(orderRejectRequest)
            }
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun onProductCheckChanged() {
        childViews?.fl_btn_primary?.isEnabled = somBottomSheetStockEmptyAdapter.getListProductEmptied().isNotEmpty()
    }

    private fun reset() {
        somBottomSheetStockEmptyAdapter.reset()
        childViews?.tf_extra_notes?.textFieldInput?.setText("")
        childViews?.fl_btn_primary?.isEnabled = false
    }

    private fun setupTicker() {
        childViews?.run {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.show()
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.gone()
            }
        }
    }

    fun setProducts(listProduct: List<SomDetailOrder.Data.GetSomDetail.Products>) {
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
}