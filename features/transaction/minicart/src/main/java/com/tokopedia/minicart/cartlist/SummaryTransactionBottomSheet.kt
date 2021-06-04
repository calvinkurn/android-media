package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class SummaryTransactionBottomSheet @Inject constructor() {

    private var textPriceTotalTitle: Typography? = null
    private var textPriceTotalValue: Typography? = null
    private var textDiscountTotalTitle: Typography? = null
    private var textDiscountTotalValue: Typography? = null
    private var textTotalPayTitle: Typography? = null
    private var textTotalPayValue: Typography? = null

    fun show(data: MiniCartSummaryTransactionUiModel, fragmentManager: FragmentManager, context: Context) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.showHeader = true
        bottomSheet.setTitle("Ringkasan belanja")

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_summary_transaction, null)
        textPriceTotalTitle = view.findViewById(R.id.text_price_total_title)
        textPriceTotalValue = view.findViewById(R.id.text_price_total_value)
        textDiscountTotalTitle = view.findViewById(R.id.text_discount_total_title)
        textDiscountTotalValue = view.findViewById(R.id.text_discount_total_value)
        textTotalPayTitle = view.findViewById(R.id.text_total_pay_title)
        textTotalPayValue = view.findViewById(R.id.text_total_pay_value)

        renderPriceTotal(data)
        renderDiscount(data)
        renderPaymentTotal(data)

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, "Mini Cart Summary Transaction")
    }

    private fun renderPaymentTotal(data: MiniCartSummaryTransactionUiModel) {
        textTotalPayTitle?.apply {
            data.paymentTotalWording.let {
                text = it
            }
        }
        textTotalPayValue?.apply {
            val totalPay = data.paymentTotal
            this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPay, false)
        }
    }

    private fun renderDiscount(data: MiniCartSummaryTransactionUiModel) {
        if (data.discountValue > 0) {
            textDiscountTotalValue?.apply {
                text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.discountValue * -1, false)
                visibility = View.VISIBLE
            }
            textDiscountTotalTitle?.apply {
                text = data.discountTotalWording
                visibility = View.VISIBLE
            }
        } else {
            textDiscountTotalValue?.apply {
                visibility = View.GONE
            }
            textDiscountTotalTitle?.apply {
                visibility = View.GONE
            }
        }
    }

    private fun renderPriceTotal(data: MiniCartSummaryTransactionUiModel) {
        textPriceTotalTitle?.apply {
            data.totalWording.let {
                text = it.replace("[0-9]".toRegex(), data.qty.toString())
            }
        }
        textPriceTotalValue?.apply {
            this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalValue, false)
        }
    }

}