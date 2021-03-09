package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.adapter.cart.CartPromoSummaryAdapter
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
    if (cartListData.promoSummaryData.details.isNotEmpty() ||
            cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        view.separator_benefit.show()
    } else {
        view.separator_benefit.gone()
    }
}

private fun renderSellerCashback(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        view.text_total_cashback_value?.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.sellerCashbackValue, false)
                    .replace("Rp", "")
                    .removeDecimalSuffix()
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
    with(view) {
        if (cartListData.promoSummaryData.details.isNotEmpty()) {
            if (cartListData.promoSummaryData.title.isNotEmpty()) {
                text_summary_promo_transaction_title.text = cartListData.promoSummaryData.title
                text_summary_promo_transaction_title.visibility = View.VISIBLE
            } else {
                text_summary_promo_transaction_title.visibility = View.GONE
            }

            val adapter = CartPromoSummaryAdapter(cartListData.promoSummaryData.details)

            recycler_view_cart_promo_summary.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recycler_view_cart_promo_summary.setHasFixedSize(true)
            recycler_view_cart_promo_summary.adapter = adapter

            recycler_view_cart_promo_summary.visibility = View.VISIBLE
        } else {
            text_summary_promo_transaction_title.visibility = View.GONE
            recycler_view_cart_promo_summary.visibility = View.GONE
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
        val totalPay = cartListData.shoppingSummaryData.paymentTotal
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPay, false).removeDecimalSuffix()
    }
}

private fun renderDiscount(cartListData: CartListData, view: View) {
    if (cartListData.shoppingSummaryData.discountValue > 0) {
        view.text_discount_total_value?.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.discountValue * -1, false).removeDecimalSuffix()
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
            text = it.replace("[0-9]+".toRegex(), cartListData.shoppingSummaryData.qty)
        }
    }
    view.text_price_total_value?.apply {
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.totalValue, false).removeDecimalSuffix()
    }
}