package com.tkpd.atcvariant.view.viewholder

import android.graphics.Paint
import android.view.View
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.ProductHeaderData
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_IMAGE_ONLY
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_PRICE_ONLY
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheetListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 07/05/21
 */

class AtcVariantHeaderViewHolder(private val view: View,
                                 private val listener: AtcVariantListener,
                                 private val atcVarBottomSheetListener: AtcVariantBottomSheetListener) : AbstractViewHolder<VariantHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_header_viewholder
    }

    private val productImage = view.findViewById<ImageUnify>(R.id.img_header_main)
    private val productPrice = view.findViewById<Typography>(R.id.txt_header_main_price)
    private val productSlashPrice = view.findViewById<Typography>(R.id.txt_header_slash_price)
    private val productStock = view.findViewById<Typography>(R.id.txt_header_stock)
    private val labelDiscount = view.findViewById<Label>(R.id.lbl_header_discounted_percentage)
    private val labelCashback = view.findViewById<Label>(R.id.lbl_header_cashback_percentage)
    private val iconEnlarge = view.findViewById<IconUnify>(R.id.ic_enlarge_img_header)

    private val labelVar1 = view.findViewById<Label>(R.id.lbl_variant_name_1)
    private val labelVar2 = view.findViewById<Label>(R.id.lbl_variant_name_2)
    private val txtTokoCabang = view.findViewById<Typography>(R.id.txt_var_warehouse)

    override fun bind(element: VariantHeaderDataModel) {
        loadImage(element.productImage)
        loadDescription(element.headerData)
        renderVariantName(element.listOfVariantTitle)
        renderTokoCabang(element.isTokoCabang, element.uspImageUrl)
        renderCashback(element.cashBackPercentage)
    }

    private fun renderCashback(cashBackPercentage: Int) = with(view) {
        labelCashback.shouldShowWithAction(cashBackPercentage > 0) {
            labelCashback.text = context.getString(com.tokopedia.product.detail.common.R.string.template_cashback, cashBackPercentage.toString())
        }
    }

    override fun bind(element: VariantHeaderDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isEmpty()) {
            return
        }

        renderVariantName(element.listOfVariantTitle)
        renderTokoCabang(element.isTokoCabang, element.uspImageUrl)
        when (payloads[0] as Int) {
            PAYLOAD_UPDATE_IMAGE_ONLY -> {
                loadImage(element.productImage)
            }
            PAYLOAD_UPDATE_PRICE_ONLY -> {
                loadDescription(element.headerData)
            }
        }
    }

    private fun renderTokoCabang(isTokoCabang: Boolean, uspImageUrl: String) = with(view) {
        txtTokoCabang?.run {
            shouldShowWithAction(isTokoCabang) {
                setOnClickListener {
                    atcVarBottomSheetListener.onTokoCabangClicked(uspImageUrl)
                }
            }
        }
    }

    private fun renderVariantName(listName: List<String>) = with(view) {
        labelVar1?.shouldShowWithAction(listName.getOrNull(0)?.isNotEmpty() == true) {
            labelVar1.text = listName.getOrNull(0) ?: ""
        }

        labelVar2?.shouldShowWithAction(listName.getOrNull(1)?.isNotEmpty() == true) {
            labelVar2.text = listName.getOrNull(1) ?: ""
        }
    }

    private fun loadDescription(headerData: ProductHeaderData) = with(view) {
        if (headerData.isCampaignActive) {
            renderCampaignActive(headerData)
        } else if (headerData.hideGimmick) {
            renderNoCampaign(headerData.productSlashPrice)
        } else {
            renderNoCampaign(headerData.productMainPrice)
        }

        productStock.shouldShowWithAction(headerData.productStockFmt.isNotEmpty() && headerData.productStockFmt != "0") {
            productStock.text = headerData.productStockFmt
        }
    }

    private fun loadImage(imgUrl: String) {

        iconEnlarge.setBackgroundResource(com.tokopedia.product.detail.common.R.drawable.bg_circle_grey)

        productImage?.run {
            setImageUrl(imgUrl)
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

        labelDiscount.shouldShowWithAction(headerData.productDiscountedPercentage != Float.ZERO) {
            labelDiscount.text = context.getString(com.tokopedia.product.detail.common.R.string.template_campaign_off, headerData.productDiscountedPercentage)
        }
    }
}
