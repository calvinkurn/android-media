package com.tokopedia.minicart.cartlist.subpage.summarytransaction

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartSummaryTransactionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class SummaryTransactionBottomSheet @Inject constructor() {

    fun show(data: MiniCartSummaryTransactionUiModel, fragmentManager: FragmentManager, context: Context) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.showHeader = true
        bottomSheet.setTitle(context.getString(R.string.mini_cart_title_summary_transaction))

        val viewBinding = LayoutBottomsheetMiniCartSummaryTransactionBinding.inflate(LayoutInflater.from(context))
        renderPriceTotal(viewBinding, data)
        renderDiscount(viewBinding, data)
        renderPaymentTotal(viewBinding, data)
        renderSellerCashback(viewBinding, data)

        bottomSheet.setChild(viewBinding.root)
        bottomSheet.show(fragmentManager, "Mini Cart Summary Transaction")
    }

    private fun renderPaymentTotal(viewBinding: LayoutBottomsheetMiniCartSummaryTransactionBinding, data: MiniCartSummaryTransactionUiModel) {
        with(viewBinding) {
            textTotalPayTitle.text = data.paymentTotalWording
            textTotalPayValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.paymentTotal, false)
        }
    }

    private fun renderDiscount(viewBinding: LayoutBottomsheetMiniCartSummaryTransactionBinding, data: MiniCartSummaryTransactionUiModel) {
        with(viewBinding) {
            if (data.discountValue > 0) {
                textDiscountTotalValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.discountValue * -1, false)
                textDiscountTotalValue.show()
                textDiscountTotalTitle.text = data.discountTotalWording
                textDiscountTotalTitle.show()
            } else {
                textDiscountTotalValue.gone()
                textDiscountTotalTitle.gone()
            }
        }
    }

    private fun renderPriceTotal(viewBinding: LayoutBottomsheetMiniCartSummaryTransactionBinding, data: MiniCartSummaryTransactionUiModel) {
        with(viewBinding) {
            textPriceTotalTitle.text = data.totalWording.replace("[0-9]+".toRegex(), data.qty.toString())
            textPriceTotalValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalValue, false)
        }
    }

    private fun renderSellerCashback(viewBinding: LayoutBottomsheetMiniCartSummaryTransactionBinding, data: MiniCartSummaryTransactionUiModel) {
        with(viewBinding) {
            if (data.sellerCashbackValue > 0) {
                textTotalCashbackTitle.text = data.sellerCashbackWording
                textTotalCashbackValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.sellerCashbackValue, false).replace("Rp", "")
                textTotalCashbackTitle.show()
                textTotalCashbackValue.show()
                separatorSellerCashback.show()
            } else {
                textTotalCashbackTitle.gone()
                textTotalCashbackValue.gone()
                separatorSellerCashback.gone()
            }
        }
    }
}