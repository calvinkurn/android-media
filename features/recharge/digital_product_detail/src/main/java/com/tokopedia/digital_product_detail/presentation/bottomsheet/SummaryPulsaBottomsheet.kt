package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital_product_detail.databinding.BottomSheetSummaryPulsaBinding
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SummaryPulsaBottomsheet(private val title:String, private val product: CatalogProduct): BottomSheetUnify() {

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

    private fun initView(){
        binding = BottomSheetSummaryPulsaBinding.inflate(LayoutInflater.from(context))
        binding?.run{
            tgTotalPay.text = if (!product.attributes.promo?.newPrice.isNullOrEmpty()){
                product.attributes.promo?.newPrice
            } else product.attributes.price

            tgTotalPrice.text = product.attributes.price

            if (product.attributes.promo?.newPricePlain.isMoreThanZero() &&
                product.attributes.pricePlain.toIntOrZero().isMoreThanZero()){
                tgTotalDiscount.show()
                tgTotalDiscountTitle.show()
                tgTotalDiscount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(((product.attributes.promo?.newPricePlain?:0).toLong() - product.attributes.pricePlain.toIntOrZero().toLong()), false)
            }
        }

        setTitle(title)
        setChild(binding?.root)
    }
}