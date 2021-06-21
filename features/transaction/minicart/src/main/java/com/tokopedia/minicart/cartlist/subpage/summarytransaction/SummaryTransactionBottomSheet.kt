package com.tokopedia.minicart.cartlist.subpage.summarytransaction

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
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
    private var separatorSellerCashback: View? = null
    private var textTotalCashbackTitle: Typography? = null
    private var textTotalCashbackValue: Typography? = null

    fun show(data: MiniCartSummaryTransactionUiModel, fragmentManager: FragmentManager, context: Context) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.showHeader = true
        bottomSheet.setTitle(context.getString(R.string.mini_cart_title_summary_transaction))

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_summary_transaction, null)
        initializeView(view)

        renderPriceTotal(data)
        renderDiscount(data)
        renderPaymentTotal(data)
        renderSellerCashback(data)

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, "Mini Cart Summary Transaction")
    }

    private fun initializeView(view: View) {
        textPriceTotalTitle = view.findViewById(R.id.text_price_total_title)
        textPriceTotalValue = view.findViewById(R.id.text_price_total_value)
        textDiscountTotalTitle = view.findViewById(R.id.text_discount_total_title)
        textDiscountTotalValue = view.findViewById(R.id.text_discount_total_value)
        textTotalPayTitle = view.findViewById(R.id.text_total_pay_title)
        textTotalPayValue = view.findViewById(R.id.text_total_pay_value)
        separatorSellerCashback = view.findViewById(R.id.separator_seller_cashback)
        textTotalCashbackTitle = view.findViewById(R.id.text_total_cashback_title)
        textTotalCashbackValue = view.findViewById(R.id.text_total_cashback_value)
    }

    private fun renderPaymentTotal(data: MiniCartSummaryTransactionUiModel) {
        textTotalPayTitle?.text = data.paymentTotalWording
        textTotalPayValue?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.paymentTotal, false)
    }

    private fun renderDiscount(data: MiniCartSummaryTransactionUiModel) {
        if (data.discountValue > 0) {
            textDiscountTotalValue?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.discountValue * -1, false)
            textDiscountTotalValue?.show()
            textDiscountTotalTitle?.text = data.discountTotalWording
            textDiscountTotalTitle?.show()
        } else {
            textDiscountTotalValue?.gone()
            textDiscountTotalTitle?.gone()
        }
    }

    private fun renderPriceTotal(data: MiniCartSummaryTransactionUiModel) {
        textPriceTotalTitle?.text = data.totalWording.replace("[0-9]+".toRegex(), data.qty.toString())
        textPriceTotalValue?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalValue, false)
    }

    private fun renderSellerCashback(data: MiniCartSummaryTransactionUiModel) {
        if (data.sellerCashbackValue > 0) {
            textTotalCashbackTitle?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.sellerCashbackValue, false).replace("Rp", "")
            textTotalCashbackTitle?.text = data.sellerCashbackWording
        } else {
            separatorSellerCashback?.gone()
            textTotalCashbackTitle?.gone()
            textTotalCashbackValue?.gone()
        }
    }
}