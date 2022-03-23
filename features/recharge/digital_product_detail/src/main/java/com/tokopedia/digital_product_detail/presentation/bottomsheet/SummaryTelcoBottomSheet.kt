package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.digital_product_detail.databinding.BottomSheetSummaryPulsaBinding
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SummaryTelcoBottomSheet(private val title:String, private val denom: DenomData): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
    }

    private var binding by autoClearedNullable<BottomSheetSummaryPulsaBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
    }

    private fun initView() {
        binding = BottomSheetSummaryPulsaBinding.inflate(LayoutInflater.from(context))
        binding?.run{
            tgTotalPay.text = denom.price
            tgTotalPrice.text = if (!denom.slashPrice.isNullOrEmpty())
                denom.slashPrice else denom.price

            if (denom.pricePlain.isMoreThanZero() && denom.slashPricePlain.isMoreThanZero()) {
                tgTotalDiscount.show()
                tgTotalDiscountTitle.show()
                tgTotalDiscount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    (denom.pricePlain.toLong() - denom.slashPricePlain.toLong()), false)
            }
        }

        setTitle(title)
        setChild(binding?.root)
    }
}