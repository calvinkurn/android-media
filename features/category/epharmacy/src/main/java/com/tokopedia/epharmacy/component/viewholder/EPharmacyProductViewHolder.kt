package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

class EPharmacyProductViewHolder(private val view: View,
                                 private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.product_name)
    private val shopNameText = view.findViewById<Typography>(R.id.shop_name)
    private val shopLocationText = view.findViewById<Typography>(R.id.shop_location)
    private val shopIcon = view.findViewById<IconUnify>(R.id.shop_icon)
    private val productQuantity = view.findViewById<Typography>(R.id.product_quantity)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val productCard = view.findViewById<CardUnify2>(R.id.product_image_card)

    companion object {
        val LAYOUT = R.layout.epharmacy_product_view_item
    }

    override fun bind(element: EPharmacyProductDataModel) {
        renderProductData(element)
    }

    private fun renderProductData(dataModel: EPharmacyProductDataModel) {
        dataModel.product?.apply {
            productCard.cardType = CardUnify2.TYPE_BORDER_ACTIVE
            productText.text = name ?: ""
            productQuantity.text = quantity.toString()
            productImageUnify.loadImage(productImage)
            shopNameText.displayTextOrHide(shopName ?: "")
            shopLocationText.displayTextOrHide(shopLocation ?: "")
            if(shopType.isNullOrBlank()){
                shopIcon.hide()
            }else{
                shopIcon.show()
                when(shopType){
                    "Regular Merchant" -> shopIcon.setImage(IconUnify.BADGE_OS)
                    "Power Merchant" -> shopIcon.setImage(IconUnify.BADGE_PM_FILLED)
                    "Official Store" -> shopIcon.setImage(IconUnify.BADGE_OS_FILLED)
                    "Power Merchant Pro" -> shopIcon.setImage(IconUnify.BADGE_OS_FILLED)
                }
            }
        }
    }
}