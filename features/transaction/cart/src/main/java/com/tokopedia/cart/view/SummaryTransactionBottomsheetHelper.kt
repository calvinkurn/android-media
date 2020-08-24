package com.tokopedia.cart.view

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.layout_bottomsheet_summary_transaction.view.*

fun showSummaryTransactionBottomsheet(cartListData: CartListData, fragmentManager: FragmentManager, context: Context) {

    val bottomSheet = BottomSheetUnify()
    bottomSheet.showKnob = true
    bottomSheet.showCloseIcon = false
    bottomSheet.showHeader = false

    val view = View.inflate(context, R.layout.layout_bottomsheet_summary_transaction, null)

    renderPriceTotal(view, cartListData)
    renderDiscount(cartListData, view)
    renderPaymentTotal(view, cartListData)
    renderPromo(cartListData, view)
    renderSellerCashback(cartListData, view)
    renderSeparatorBenefit(cartListData, view)

    bottomSheet.setChild(view)
    bottomSheet.show(fragmentManager, "Cart Summary Transaction")
}

private fun renderSeparatorBenefit(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.promoValue > 0 ||
            cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        view.separator_benefit.show()
    } else {
        view.separator_benefit.gone()
    }
}

private fun renderSellerCashback(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        view.text_total_cashback_value?.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.sellerCashbackValue, false).removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        view.text_total_cashback_title?.apply {
            text = cartListData.shoppingSummaryData.sellerCashbackWording
            visibility = View.VISIBLE
        }
    } else {
        view.text_total_cashback_value?.apply {
            visibility = View.GONE
        }
        view.text_total_cashback_title?.apply {
            visibility = View.GONE
        }
    }
}

private fun renderPromo(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.promoValue > 0) {
        view.text_total_promo_value?.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.promoValue, false).removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        view.text_total_promo_title?.apply {
            text = cartListData.shoppingSummaryData.promoWording
            visibility = View.VISIBLE
        }
    } else {
        view.text_total_promo_value?.apply {
            visibility = View.GONE
        }
        view.text_total_promo_title?.apply {
            visibility = View.GONE
        }
    }
}

private fun renderPaymentTotal(view: View, cartListData: CartListData) {
    view.text_total_pay_title?.apply {
        cartListData.shoppingSummaryData.paymentTotalWording.let {
            text = it
        }
    }
    view.text_total_pay_value?.apply {
        val totalPay = cartListData.shoppingSummaryData.totalValue - cartListData.shoppingSummaryData.discountValue
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPay, false).removeDecimalSuffix()
    }
}

private fun renderDiscount(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.discountValue > 0) {
        view.text_discount_total_value?.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.discountValue, false).removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        view.text_discount_total_title?.apply {
            text = cartListData.shoppingSummaryData.discountTotalWording
            visibility = View.VISIBLE
        }
    } else {
        view.text_discount_total_value?.apply {
            visibility = View.GONE
        }
        view.text_discount_total_title?.apply {
            visibility = View.GONE
        }
    }
}

private fun renderPriceTotal(view: View, cartListData: CartListData) {
    view.text_price_total_title?.apply {
        cartListData.shoppingSummaryData.totalWording.let {
            text = it.replace("x", cartListData.shoppingSummaryData.qty)
        }
    }
    view.text_price_total_value?.apply {
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.totalValue, false).removeDecimalSuffix()
    }
}