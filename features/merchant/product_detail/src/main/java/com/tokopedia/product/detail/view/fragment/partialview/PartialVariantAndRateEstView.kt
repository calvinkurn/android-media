package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.ShopCommitment
import com.tokopedia.product.detail.estimasiongkir.data.model.RatesModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.holder.RatesEstSummarize
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.view.*

class PartialVariantAndRateEstView private constructor(private val view: View) {

    companion object {
        fun build(_view: View) = PartialVariantAndRateEstView(_view)
    }

    fun renderData(productVariant: ProductVariant?, selectedOptionString: String, onVariantClickedListener: (() -> Unit)? = null) {
        with(view) {
            if (productVariant != null) {
                label_variant.visible()
                label_choose_variant.visible()
                if (txt_rate_estimation_start.isVisible || txt_courier_dest.isVisible) {
                    variant_divider.visible()
                } else {
                    variant_divider.gone()
                }
                label_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                label_choose_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                val chooseString =
                    if (selectedOptionString.isEmpty()) {
                        "${view.context.getString(R.string.choose)} " +
                            productVariant.variant.map { it.name }.joinToStringWithLast(separator = ", ",
                                lastSeparator = " ${view.context.getString(R.string.and)} ")
                    } else {
                        selectedOptionString
                    }
                label_choose_variant.text = chooseString
                visible()
            } else {
                label_variant.gone()
                label_choose_variant.gone()
                variant_divider.gone()
            }
        }

    }

    fun renderRateEstimation(summarize: RatesEstSummarize, shopLocation: String, onRateEstimationClicked: (()-> Unit)? = null) {
        if (summarize.addressDest.isBlank()) return

        with(view){
            txt_rate_estimation_start.text = context.getString(R.string.template_rates_estimate_start_price,
                    summarize.minPrice.getCurrencyFormatted())
            txt_rate_estimation_start.visible()
            icon_shop_location.visible()
            txt_shop_location.text = shopLocation
            txt_shop_location.visible()
            icon_courier_est.visible()
            txt_courier_dest.text = summarize.addressDest
            txt_courier_dest.visible()

            if (label_variant.isVisible) {
                variant_divider.visible()
            } else {
                variant_divider.gone()
            }
            visible()
            setOnClickListener { onRateEstimationClicked?.invoke() }
        }
    }

    fun renderPriorityOrder(shopCommitment: ShopCommitment) {
        with(view) {
            if (shopCommitment.isNowActive) {
                if (label_variant.isVisible || txt_courier_dest.isVisible) {
                    priority_order_divider.visible()
                } else {
                    priority_order_divider.gone()
                }
                icon_priority_order.visible()
                txt_priority_order_title.visible()
                txt_priority_order_message.visible()
                txt_priority_order_message.text = MethodChecker.fromHtml(shopCommitment.staticMessages.pdpMessage)
                visible()
            } else {
                priority_order_divider.gone()
                icon_priority_order.gone()
                txt_priority_order_title.gone()
                txt_priority_order_message.gone()
            }
        }
    }


}
