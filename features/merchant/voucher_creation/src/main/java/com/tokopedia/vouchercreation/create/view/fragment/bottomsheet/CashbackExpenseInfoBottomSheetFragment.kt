package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel
import kotlinx.android.synthetic.main.mvc_cashback_expense_info.*

class CashbackExpenseInfoBottomSheetFragment : BottomSheetUnify(), VoucherBottomView {

    override var bottomSheetViewTitle: String? = ""

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           getCashbackInfo: () -> CashbackPercentageInfoUiModel): CashbackExpenseInfoBottomSheetFragment {
            return CashbackExpenseInfoBottomSheetFragment().apply {
                val view = View.inflate(context, R.layout.mvc_cashback_expense_info, null)
                setChild(view)
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                this.getCashbackInfo = getCashbackInfo
            }
        }

        const val TAG = "CashbackExpenseInfoBottomSheet"
    }

    private var getCashbackInfo: () -> CashbackPercentageInfoUiModel = {
        CashbackPercentageInfoUiModel(0, 0, 0, 0)
    }

    private var onEditButtonClicked: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            initView()
        }
    }

    fun setEditNowButtonClicked(action: () -> Unit): CashbackExpenseInfoBottomSheetFragment {
        onEditButtonClicked = action
        return this
    }

    private fun initView() {
        val title = context?.getString(R.string.mvc_create_promo_type_bottomsheet_subtitle_increase_discount)
                ?: ""
        this.setTitle(title)
        getCashbackInfo().run {
            minimumPurchaseInfoValue?.text = String.format(
                    context?.getString(R.string.mvc_rp_value).toBlankOrString(),
                    CurrencyFormatHelper.convertToRupiah(minimumPurchase.toString())).toBlankOrString()
            percentageInfoValue?.text = "$cashbackPercentage%"
            infoDiscountValue?.text = String.format(context?.getString(R.string.mvc_create_promo_type_bottomsheet_discount_value).toBlankOrString(), CurrencyFormatHelper.convertToRupiah(minimumDiscount.toString()))
            val description = String.format(context?.getString(R.string.mvc_create_promo_type_bottomsheet_desc).toBlankOrString(), CurrencyFormatHelper.convertToRupiah(minimumDiscount.toString())).parseAsHtml()
            cashbackExpenseDescription?.text = description
            cashbackExpenseButton?.setOnClickListener {
                onEditButtonClicked()
                this@CashbackExpenseInfoBottomSheetFragment.dismiss()
            }
        }
    }
}