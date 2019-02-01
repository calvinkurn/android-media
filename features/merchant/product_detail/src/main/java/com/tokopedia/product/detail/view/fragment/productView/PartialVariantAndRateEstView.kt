package com.tokopedia.product.detail.view.fragment.productView

import android.view.View
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.view.*

class PartialVariantAndRateEstView private constructor(private val view: View) {

    var productVariant: ProductVariant? = null

    companion object {
        fun build(_view: View) = PartialVariantAndRateEstView(_view)
    }

    fun renderData(onVariantClickedListener: (()->Unit)? = null) {
        //TODO hide/show logic for variant/rate/courier/etc
        with(view) {
            var hasData = false
            if (productVariant == null) {
                label_variant.visibility = View.GONE
            } else {
                label_variant.visibility = View.VISIBLE
                label_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                label_choose_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                hasData = true
            }
            visibility = if (hasData) View.VISIBLE else View.GONE
        }

    }


}