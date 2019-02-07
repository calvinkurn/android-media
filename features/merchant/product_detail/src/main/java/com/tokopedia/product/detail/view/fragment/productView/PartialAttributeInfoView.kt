package com.tokopedia.product.detail.view.fragment.productView

import android.view.View
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.util.successRate
import com.tokopedia.product.detail.data.util.thousandFormatted
import kotlinx.android.synthetic.main.partial_product_detail_visibility.view.*

class PartialAttributeInfoView private constructor(private val view: View){
    companion object {
        fun build(_view: View) = PartialAttributeInfoView(_view)
    }

    fun renderData(productInfo: ProductInfo){
        with(view){
            txt_seen.text = productInfo.stats.countView.thousandFormatted()
            txt_tx_success.text = context.getString(R.string.template_success_rate,
                    productInfo.txStats.successRate, productInfo.txStats.sold.thousandFormatted())
            base_attribute_info.visible()
        }
    }
}