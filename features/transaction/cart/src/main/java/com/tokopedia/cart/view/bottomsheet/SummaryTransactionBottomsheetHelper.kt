package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.LayoutBottomsheetSummaryTransactionBinding
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.adapter.cart.CartPromoSummaryAdapter
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify

fun showSummaryTransactionBottomsheet(cartListData: CartListData, fragmentManager: FragmentManager, context: Context) {

    val bottomSheet = BottomSheetUnify()
    bottomSheet.showKnob = true
    bottomSheet.showCloseIcon = false
    bottomSheet.showHeader = false

    val binding = LayoutBottomsheetSummaryTransactionBinding.inflate(LayoutInflater.from(context))

    renderPriceTotal(binding, cartListData)
    renderDiscount(cartListData, binding)
    renderPaymentTotal(binding, cartListData)
    renderPromo(cartListData, binding)
    renderSellerCashback(cartListData, binding)
    renderSeparatorBenefit(cartListData, binding)

    bottomSheet.setChild(binding.root)
    bottomSheet.show(fragmentManager, "Cart Summary Transaction")
}

private fun renderSeparatorBenefit(cartListData: CartListData, binding: LayoutBottomsheetSummaryTransactionBinding) {
    if (cartListData.promoSummaryData.details.isNotEmpty() ||
            cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        binding.separatorBenefit.show()
    } else {
        binding.separatorBenefit.gone()
    }
}

private fun renderSellerCashback(cartListData: CartListData, binding: LayoutBottomsheetSummaryTransactionBinding) {
    if (cartListData.shoppingSummaryData.sellerCashbackValue > 0) {
        binding.textTotalCashbackValue.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.sellerCashbackValue, false)
                    .replace("Rp", "")
                    .removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textTotalCashbackTitle.apply {
            text = cartListData.shoppingSummaryData.sellerCashbackWording
            visibility = View.VISIBLE
        }
        view.separator_seller_cashback?.apply {
            visibility = View.VISIBLE
        }
    } else {
        view.separator_seller_cashback?.apply {
            visibility = View.GONE
        }
        view.text_total_cashback_value?.apply {
            visibility = View.GONE
        }
        binding.textTotalCashbackTitle.apply {
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

private fun renderPaymentTotal(binding: LayoutBottomsheetSummaryTransactionBinding, cartListData: CartListData) {
    binding.textTotalPayTitle.apply {
        cartListData.shoppingSummaryData.paymentTotalWording.let {
            text = it
        }
    }
    binding.textTotalPayValue.apply {
        val totalPay = cartListData.shoppingSummaryData.paymentTotal
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPay, false).removeDecimalSuffix()
    }
}

private fun renderDiscount(cartListData: CartListData, binding: LayoutBottomsheetSummaryTransactionBinding) {
    if (cartListData.shoppingSummaryData.discountValue > 0) {
        binding.textDiscountTotalValue.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.discountValue * -1, false).removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textDiscountTotalTitle.apply {
            text = cartListData.shoppingSummaryData.discountTotalWording
            visibility = View.VISIBLE
        }
    } else {
        binding.textDiscountTotalValue.apply {
            visibility = View.GONE
        }
        binding.textDiscountTotalTitle.apply {
            visibility = View.GONE
        }
    }
}

private fun renderPriceTotal(binding: LayoutBottomsheetSummaryTransactionBinding, cartListData: CartListData) {
    binding.textPriceTotalTitle.apply {
        cartListData.shoppingSummaryData.totalWording.let {
            text = it.replace("[0-9]+".toRegex(), cartListData.shoppingSummaryData.qty)
        }
    }
    binding.textPriceTotalValue.apply {
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.totalValue, false).removeDecimalSuffix()
    }
}