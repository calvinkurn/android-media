package com.tokopedia.product.detail.view.fragment.productView

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.data.model.estimasiongkir.RatesModel
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.view.*

class PartialVariantAndRateEstView private constructor(private val view: View) {
    private var hasData: Boolean = false
    var productVariant: ProductVariant? = null

    companion object {
        fun build(_view: View) = PartialVariantAndRateEstView(_view)
    }

    fun renderData(onVariantClickedListener: (()->Unit)? = null) {
        //TODO hide/show logic for variant/rate/courier/etc
        with(view) {
            if (productVariant == null) {
                label_variant.visibility = View.GONE
            } else {
                label_variant.visibility = View.VISIBLE
                label_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                label_choose_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                hasData = true
            }
            if (hasData) visible() else gone()
        }

    }

    fun renderRateEstimation(ratesModel: RatesModel, shopLocation: String) {
        if (ratesModel.id.isBlank()) return

        with(view){
            hasData = true
            txt_rate_estimation_start.text = MethodChecker.fromHtml(ratesModel.texts.textMinPrice)
            txt_rate_estimation_start.visible()
            icon_shop_location.visible()
            txt_shop_location.text = shopLocation
            txt_shop_location.visible()
            icon_courier_est.visible()
            txt_courier_dest.text = ratesModel.texts.textDestination
            txt_courier_dest.visible()

            if (label_variant.isVisible){
                variant_divider.visible()
            } else {
                variant_divider.gone()
            }


            if (hasData) visible() else gone()
        }
    }


}