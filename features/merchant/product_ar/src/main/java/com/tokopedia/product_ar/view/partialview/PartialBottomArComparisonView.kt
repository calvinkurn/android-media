package com.tokopedia.product_ar.view.partialview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class PartialBottomArComparisonView private constructor(val view: View, val listener: ProductArListener) {

    companion object {
        fun build(view: View, listener: ProductArListener) = PartialBottomArComparisonView(view, listener)
    }

    private val atcButton = view.findViewById<UnifyButton>(R.id.btn_atc_ar)
    private val txtStock = view.findViewById<Typography>(R.id.txt_stock_ar)
    private val txtMainPrice = view.findViewById<Typography>(R.id.txt_main_price_ar)
    private val txtSlashPrice = view.findViewById<Typography>(R.id.txt_slash_price_ar)
    private val lblDiscounted = view.findViewById<Label>(R.id.lbl_discounted_ar)
    private val rvVariant = view.findViewById<RecyclerView>(R.id.rv_ar)

}