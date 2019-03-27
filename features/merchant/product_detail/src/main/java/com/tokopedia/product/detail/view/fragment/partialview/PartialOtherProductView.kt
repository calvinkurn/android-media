package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.data.model.product.ProductOther
import com.tokopedia.product.detail.view.adapter.OtherProductAdapter
import kotlinx.android.synthetic.main.partial_other_product.view.*

class PartialOtherProductView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialOtherProductView(_view)
    }

    init {
        with(view){
            other_products.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun renderData(products: List<ProductOther>){
        with(view) {
            if (products.isEmpty())
                gone()
            else {
                other_products.adapter = OtherProductAdapter(products)
                visible()
            }
        }

    }
}