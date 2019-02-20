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
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.view.*

class PartialVariantAndRateEstView private constructor(private val view: View) {

    companion object {
        fun build(_view: View) = PartialVariantAndRateEstView(_view)
    }

    fun renderData(productVariant: ProductVariant?, onVariantClickedListener: (()->Unit)? = null) {
        //TODO hide/show logic for variant/rate/courier/etc
        with(view) {
            if (productVariant != null) {
                label_variant.visible()
                label_choose_variant.visible()
                variant_divider.visible()
                label_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                label_choose_variant.setOnClickListener { onVariantClickedListener?.invoke() }
                val chooseString = "${view.context.getString(R.string.choose)} " +
                        "${productVariant.variant?.map { it.name }?.joinToStringWithLast(separator = ", ",
                                lastSeparator = " ${view.context.getString(R.string.and)} ")}"
                label_choose_variant.text = chooseString
                visible()
            } else {
                label_variant.gone()
                label_choose_variant.gone()
                variant_divider.gone()
            }
        }

    }

    fun renderRateEstimation(ratesModel: RatesModel, shopLocation: String, onRateEstimationClicked: (()-> Unit)? = null) {
        if (ratesModel.id.isBlank()) return

        with(view){
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
            visible()
            setOnClickListener { onRateEstimationClicked?.invoke() }
        }
    }

    fun renderPriorityOrder(shopCommitment: ShopCommitment) {
        with(view){
            if (shopCommitment.isNowActive){
                if (label_variant.isVisible || txt_courier_dest.isVisible){
                    priority_order_divider.visible()
                } else {
                    priority_order_divider.gone()
                }
                icon_priority_order.visible()
                txt_priority_order_title.visible()
                txt_priority_order_message.visible()
                txt_priority_order_message.text = MethodChecker.fromHtml(shopCommitment.staticMessages.pdpMessage)
            } else {
                priority_order_divider.gone()
                icon_priority_order.gone()
                txt_priority_order_title.gone()
                txt_priority_order_message.gone()
            }
        }
    }


}
