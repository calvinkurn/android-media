package com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcCashbackExpenseInfoBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel

class CashbackExpenseInfoBottomSheetFragment : BottomSheetUnify(), VoucherBottomView {

    override var bottomSheetViewTitle: String? = ""

    companion object {
        @JvmStatic
        fun createInstance(getCashbackInfo: () -> CashbackPercentageInfoUiModel): CashbackExpenseInfoBottomSheetFragment {
            return CashbackExpenseInfoBottomSheetFragment().apply {
                this.getCashbackInfo = getCashbackInfo
            }
        }

        const val TAG = "CashbackExpenseInfoBottomSheet"
    }

    private var binding by autoClearedNullable<MvcCashbackExpenseInfoBinding>()

    private var getCashbackInfo: () -> CashbackPercentageInfoUiModel = {
        CashbackPercentageInfoUiModel(0, 0, 0, 0)
    }

    private var onEditButtonClicked: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            initView()
        }
    }

    private fun iniBottomSheet() {
        binding = MvcCashbackExpenseInfoBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private fun initView() {
        val title = context?.getString(R.string.mvc_create_promo_type_bottomsheet_subtitle_increase_discount)
            ?: ""
        this.setTitle(title)
        getCashbackInfo().run {
            binding?.apply {
                minimumPurchaseInfoValue.text = String.format(
                    context?.getString(R.string.mvc_rp_value).toBlankOrString(),
                    CurrencyFormatHelper.convertToRupiah(minimumPurchase.toString())).toBlankOrString()
                percentageInfoValue.text = "$cashbackPercentage%"
                infoDiscountValue.text = String.format(context?.getString(R.string.mvc_create_promo_type_bottomsheet_discount_value).toBlankOrString(), CurrencyFormatHelper.convertToRupiah(minimumDiscount.toString()))
                val description = String.format(context?.getString(R.string.mvc_create_promo_type_bottomsheet_desc).toBlankOrString(), CurrencyFormatHelper.convertToRupiah(minimumDiscount.toString())).parseAsHtml()
                cashbackExpenseDescription.text = description
                cashbackExpenseButton.setOnClickListener {
                    onEditButtonClicked()
                    this@CashbackExpenseInfoBottomSheetFragment.dismiss()
                }
            }
        }
    }

    fun setEditNowButtonClicked(action: () -> Unit): CashbackExpenseInfoBottomSheetFragment {
        onEditButtonClicked = action
        return this
    }
}