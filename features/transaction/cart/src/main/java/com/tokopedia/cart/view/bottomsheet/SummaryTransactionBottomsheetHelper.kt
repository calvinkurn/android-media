package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.LayoutBottomsheetSummaryTransactionBinding
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.adapter.cart.CartPromoSummaryAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

fun showSummaryTransactionBottomsheet(cartListData: CartListData, fragmentManager: FragmentManager, context: Context) {

    val bottomSheet = BottomSheetUnify().apply {
        showKnob = true
        showCloseIcon = false
        showHeader = false
        isDragable = true
        isHideable = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
    }

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
        binding.separatorSellerCashback.show()
    } else {
        binding.separatorSellerCashback.gone()
        binding.textTotalCashbackValue.gone()
        binding.textTotalCashbackTitle.gone()
    }
}

private fun renderPromo(cartListData: CartListData, binding: LayoutBottomsheetSummaryTransactionBinding) {
    with(binding) {
        if (cartListData.promoSummaryData.details.isNotEmpty()) {
            if (cartListData.promoSummaryData.title.isNotEmpty()) {
                textSummaryPromoTransactionTitle.text = cartListData.promoSummaryData.title
                textSummaryPromoTransactionTitle.visibility = View.VISIBLE
            } else {
                textSummaryPromoTransactionTitle.visibility = View.GONE
            }

            val adapter = CartPromoSummaryAdapter(cartListData.promoSummaryData.details)

            recyclerViewCartPromoSummary.layoutManager = LinearLayoutManager(root.context, RecyclerView.VERTICAL, false)
            recyclerViewCartPromoSummary.setHasFixedSize(true)
            recyclerViewCartPromoSummary.adapter = adapter

            recyclerViewCartPromoSummary.visibility = View.VISIBLE
        } else {
            textSummaryPromoTransactionTitle.visibility = View.GONE
            recyclerViewCartPromoSummary.visibility = View.GONE
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