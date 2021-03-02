package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.databinding.LayoutBottomsheetSummaryTransactionBinding
import com.tokopedia.cart.domain.model.cartlist.CartListData
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
    if (cartListData.shoppingSummaryData.promoValue > 0 ||
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
                    .replace("Rp","")
                    .removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textTotalCashbackTitle.apply {
            text = cartListData.shoppingSummaryData.sellerCashbackWording
            visibility = View.VISIBLE
        }
    } else {
        binding.textTotalCashbackValue.apply {
            visibility = View.GONE
        }
        binding.textTotalCashbackTitle.apply {
            visibility = View.GONE
        }
    }
}

private fun renderPromo(cartListData: CartListData, binding: LayoutBottomsheetSummaryTransactionBinding) {
    if (cartListData.shoppingSummaryData.promoValue > 0) {
        binding.textTotalPromoValue.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.promoValue, false)
                    .replace("Rp","")
                    .removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textTotalPromoTitle.apply {
            text = cartListData.shoppingSummaryData.promoWording
            visibility = View.VISIBLE
        }
    } else {
        binding.textTotalPromoValue.apply {
            visibility = View.GONE
        }
        binding.textTotalPromoTitle.apply {
            visibility = View.GONE
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
            text = it.replace("[0-9]".toRegex(), cartListData.shoppingSummaryData.qty)
        }
    }
    binding.textPriceTotalValue.apply {
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartListData.shoppingSummaryData.totalValue, false).removeDecimalSuffix()
    }
}