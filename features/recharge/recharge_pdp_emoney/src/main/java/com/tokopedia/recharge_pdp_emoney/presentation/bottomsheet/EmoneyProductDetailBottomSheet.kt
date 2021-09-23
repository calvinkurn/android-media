package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyProductDetailBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

/**
 * @author by jessica on 09/04/21
 */

class EmoneyProductDetailBottomSheet(val product: CatalogProduct) : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view =
            WidgetEmoneyProductDetailBottomSheetBinding.inflate(LayoutInflater.from(context))
        setChild(view.root)
        setTitle(getString(R.string.recharge_pdp_emoney_product_detail_bottom_sheet_title))
        initView(view)
    }

    private fun initView(view: WidgetEmoneyProductDetailBottomSheetBinding) {
        with(view) {
            emoneyBottomSheetProductTitle.text = product.attributes.desc
            emoneyBottomSheetProductDescription.text = MethodChecker.fromHtml(product.attributes.detail)
            emoneyBottomSheetProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(product.attributes.pricePlain.toIntOrZero())
        }
    }
}