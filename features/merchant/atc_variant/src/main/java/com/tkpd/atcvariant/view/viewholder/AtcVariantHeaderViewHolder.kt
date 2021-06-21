package com.tkpd.atcvariant.view.viewholder

import android.graphics.Paint
import android.view.View
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.ProductHeaderData
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_IMAGE_ONLY
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_PRICE_ONLY
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 07/05/21
 */

class AtcVariantHeaderViewHolder(private val view: View, private val listener: AtcVariantListener) : AbstractViewHolder<VariantHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_header_viewholder
    }

    private val productImage = view.findViewById<ImageUnify>(R.id.img_header_main)
    private val productPrice = view.findViewById<Typography>(R.id.txt_header_main_price)
    private val productSlashPrice = view.findViewById<Typography>(R.id.txt_header_slash_price)
    private val productStock = view.findViewById<Typography>(R.id.txt_header_stock)
    private val labelDiscount = view.findViewById<Label>(R.id.lbl_header_discounted_percentage)

    override fun bind(element: VariantHeaderDataModel) {
        loadImage(element.productImage)
        loadDescription(element.headerData)
    }

    override fun bind(element: VariantHeaderDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_UPDATE_IMAGE_ONLY -> {
                loadImage(element.productImage)
            }
            PAYLOAD_UPDATE_PRICE_ONLY -> {
                loadDescription(element.headerData)
            }
        }

    }

    private fun loadDescription(headerData: ProductHeaderData) {
        if (headerData.isCampaignActive) {
            renderCampaignActive(headerData)
        } else {
            renderNoCampaign(headerData.productMainPrice)
        }

        productStock.shouldShowWithAction(headerData.productStockWording != "") {
            productStock.text = MethodChecker.fromHtml(headerData.productStockWording ?: "")
        }
    }

    private fun loadImage(imgUrl: String) {
        productImage.loadImage(imgUrl)
        productImage?.run {
            loadImage(imgUrl)
            setOnClickListener {
                listener.onVariantImageClicked(imgUrl)
            }
        }
    }

    private fun renderNoCampaign(price: String) = with(view) {
        labelDiscount?.hide()
        productSlashPrice?.hide()
        productPrice.text = price
    }

    private fun renderCampaignActive(headerData: ProductHeaderData) = with(view) {
        productPrice.text = headerData.productSlashPrice

        productSlashPrice.shouldShowWithAction(headerData.productSlashPrice != "") {
            productSlashPrice?.text = headerData.productMainPrice
            productSlashPrice.paintFlags = productSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        labelDiscount.shouldShowWithAction(headerData.productDiscountedPercentage != 0) {
            labelDiscount.text = context.getString(com.tokopedia.product.detail.common.R.string.template_campaign_off, headerData.productDiscountedPercentage.toString())
        }
    }

}