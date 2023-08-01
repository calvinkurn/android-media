package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.LayoutBottomsheetSummaryTransactionBinding
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.view.adapter.cart.CartAddOnSummaryAdapter
import com.tokopedia.cart.view.adapter.cart.CartPromoSummaryAdapter
import com.tokopedia.cart.view.uimodel.PromoSummaryData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

fun showSummaryTransactionBottomsheet(
    summaryTransactionUiModel: SummaryTransactionUiModel,
    promoSummaryUiModel: PromoSummaryData?,
    fragmentManager: FragmentManager,
    context: Context
) {
    val bottomSheet = BottomSheetUnify().apply {
        showKnob = true
        showCloseIcon = false
        showHeader = false
        isDragable = true
        isHideable = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
    }

    val binding = LayoutBottomsheetSummaryTransactionBinding.inflate(LayoutInflater.from(context))

    renderPriceTotal(binding, summaryTransactionUiModel)
    renderDiscount(binding, summaryTransactionUiModel)
    renderPaymentTotal(binding, summaryTransactionUiModel)
    promoSummaryUiModel?.let {
        renderPromo(binding, it)
    }
    renderSellerCashback(binding, summaryTransactionUiModel)
    renderSummaryAddon(binding, summaryTransactionUiModel)
    renderSeparatorBenefit(binding, summaryTransactionUiModel, promoSummaryUiModel)

    bottomSheet.setChild(binding.root)
    bottomSheet.show(fragmentManager, "Cart Summary Transaction")
}

private fun renderSeparatorBenefit(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel, promoSummaryUiModel: PromoSummaryData?) {
    if (promoSummaryUiModel?.details?.isNotEmpty() == true || summaryTransactionUiModel.sellerCashbackValue > 0) {
        binding.separatorBenefit.show()
    } else {
        binding.separatorBenefit.gone()
    }
}

private fun renderSellerCashback(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel) {
    if (summaryTransactionUiModel.sellerCashbackValue > 0) {
        binding.textTotalCashbackValue.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(summaryTransactionUiModel.sellerCashbackValue, false)
                .replace("Rp", "")
                .removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textTotalCashbackTitle.apply {
            text = summaryTransactionUiModel.sellerCashbackWording
            visibility = View.VISIBLE
        }
        binding.separatorSellerCashback.show()
    } else {
        binding.separatorSellerCashback.gone()
        binding.textTotalCashbackValue.gone()
        binding.textTotalCashbackTitle.gone()
    }
}

private fun renderPromo(binding: LayoutBottomsheetSummaryTransactionBinding, promoSummaryData: PromoSummaryData) {
    with(binding) {
        if (promoSummaryData.details.isNotEmpty()) {
            if (promoSummaryData.title.isNotEmpty()) {
                textSummaryPromoTransactionTitle.text = promoSummaryData.title
                textSummaryPromoTransactionTitle.visibility = View.VISIBLE
            } else {
                textSummaryPromoTransactionTitle.visibility = View.GONE
            }

            val adapter = CartPromoSummaryAdapter(promoSummaryData.details)

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

private fun renderPaymentTotal(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel) {
    binding.textTotalPayTitle.apply {
        summaryTransactionUiModel.paymentTotalWording.let {
            text = it
        }
    }
    binding.textTotalPayValue.apply {
        val totalPay = summaryTransactionUiModel.paymentTotal
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPay, false).removeDecimalSuffix()
    }
}

private fun renderDiscount(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel) {
    if (summaryTransactionUiModel.discountValue > 0) {
        binding.textDiscountTotalValue.apply {
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(summaryTransactionUiModel.discountValue * -1, false).removeDecimalSuffix()
            visibility = View.VISIBLE
        }
        binding.textDiscountTotalTitle.apply {
            text = summaryTransactionUiModel.discountTotalWording
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

private fun renderPriceTotal(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel) {
    binding.textPriceTotalTitle.apply {
        summaryTransactionUiModel.totalWording.let {
            text = it.replace("[0-9]+".toRegex(), summaryTransactionUiModel.qty)
        }
    }
    binding.textPriceTotalValue.apply {
        this.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(summaryTransactionUiModel.totalValue, false).removeDecimalSuffix()
    }
}

private fun renderSummaryAddon(binding: LayoutBottomsheetSummaryTransactionBinding, summaryTransactionUiModel: SummaryTransactionUiModel) {
    with(binding) {
        if (summaryTransactionUiModel.listSummaryAddOns.isNotEmpty()) {
            val adapter = CartAddOnSummaryAdapter(summaryTransactionUiModel.listSummaryAddOns)
            recyclerViewAddOnSummary.layoutManager = LinearLayoutManager(root.context, RecyclerView.VERTICAL, false)
            recyclerViewAddOnSummary.setHasFixedSize(true)
            recyclerViewAddOnSummary.adapter = adapter

            recyclerViewAddOnSummary.visibility = View.VISIBLE
        } else {
            recyclerViewAddOnSummary.visibility = View.GONE
        }
    }
}
