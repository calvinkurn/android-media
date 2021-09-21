package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.widget_emoney_product_detail_bottom_sheet.view.*

/**
 * @author by jessica on 09/04/21
 */

class EmoneyProductDetailBottomSheet(val product: CatalogProduct) : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
    }

    private lateinit var childView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        childView = View.inflate(context, R.layout.widget_emoney_product_detail_bottom_sheet, null)
        setChild(childView)
        setTitle(getString(R.string.recharge_pdp_emoney_product_detail_bottom_sheet_title))
        initView(childView)
    }

    private fun initView(view: View) {
        with(view) {
            emoneyBottomSheetProductTitle.text = product.attributes.desc
            emoneyBottomSheetProductDescription.text = MethodChecker.fromHtml(product.attributes.detail)
            emoneyBottomSheetProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(product.attributes.pricePlain.toIntOrZero())
        }
    }
}