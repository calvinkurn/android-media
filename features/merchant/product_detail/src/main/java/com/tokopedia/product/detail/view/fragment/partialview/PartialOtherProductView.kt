package com.tokopedia.product.detail.view.fragment.partialview

import androidx.recyclerview.widget.LinearLayoutManager
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
            loading_other_product.gone()
            if (products.isEmpty())
                gone()
            else {
                other_products.adapter = OtherProductAdapter(products)
                other_products.visible()
                visible()
            }
        }

    }

    fun startLoading() {
        with(view){
            visible()
            loading_other_product.visible()
            other_products.gone()
        }
    }
}