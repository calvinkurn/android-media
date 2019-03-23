package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.shop.ShopCommitment
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
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

    fun renderRateEstimation(summarize: SummaryText, shopLocation: String, onRateEstimationClicked: (() -> Unit)? = null) {
        if (summarize.destination.isBlank()) return

        with(view) {
            txt_rate_estimation_start.text = summarize.minPrice
            txt_rate_estimation_start.visible()
            icon_shop_location.visible()
            txt_shop_location.text = context.getString(R.string.from, shopLocation).boldPartial("dari".length)
            txt_shop_location.visible()
            icon_courier_est.visible()
            txt_courier_dest.text = context.getString(R.string.to, summarize.destination).boldPartial("ke".length)
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

    private fun String.boldPartial(from: Int, to: Int = length): SpannableString {
        val spanText = SpannableString(this)
        spanText.setSpan(StyleSpan(Typeface.BOLD), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanText
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

    fun renderPurchaseProtectionData(productInfo: ProductPurchaseProtectionInfo) {
        with(view) {
            if (productInfo.ppItemDetailPage?.isProtectionAvailable!!) {

                if (txt_courier_dest.isVisible || txt_shop_location.isVisible) {
                    purchase_protection_divider.visible()
                } else {
                    purchase_protection_divider.gone()
                }
                icon_purchase_protection.visible()
                txt_purchase_protection_title.visible()
                txt_purchase_protection_message.visible()
                base_variant.visible()

                txt_purchase_protection_title.text = productInfo.ppItemDetailPage!!.titlePDP
                txt_purchase_protection_message.text = productInfo.ppItemDetailPage!!.subTitlePDP
            } else {
                base_variant.gone()
                purchase_protection_divider.gone()
                icon_purchase_protection.gone()
                txt_purchase_protection_title.gone()
                txt_purchase_protection_message.gone()
            }
        }
    }


}
