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
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.math.BigDecimal

class EPharmacyProductViewHolder(view: View) : AbstractViewHolder<EPharmacyProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.product_name)
    private val shopNameText = view.findViewById<Typography>(R.id.shop_name)
    private val shopLocationText = view.findViewById<Typography>(R.id.shop_location)
    private val shopIcon = view.findViewById<ImageUnify>(R.id.shop_icon)
    private val productQuantity = view.findViewById<Typography>(R.id.product_quantity)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val productCard = view.findViewById<CardUnify2>(R.id.product_image_card)
    private val divider = view.findViewById<DividerUnify>(R.id.divider)

    companion object {
        val LAYOUT = R.layout.epharmacy_product_view_item
        private const val KILOGRAM_DIVIDER = 1000.0f
        private const val DIGIT_AFTER_COMMA = 2
        private const val LABEL_KILOGRAM = " kg"
        private const val LABEL_GRAM = " gr"
    }

    override fun bind(element: EPharmacyProductDataModel) {
        renderProductData(element)
    }

    private fun renderProductData(dataModel: EPharmacyProductDataModel) {
        dataModel.product?.apply {
            productCard.cardType = CardUnify2.TYPE_BORDER
            productText.text = name ?: ""
            productQuantity.text = java.lang.String.format(
                itemView.context.getString(R.string.epharmacy_quantity_weight_text),
                quantity ?: "",
                getFormattedWeight(
                    itemWeight ?: 0.0,
                    quantity ?: 0
                )
            )
            productImageUnify.loadImage(productImage)
            shopNameText.displayTextOrHide(shopName ?: "")
            shopLocationText.displayTextOrHide(shopLocation ?: "")
            renderShopIcon(shopLogoUrl)
            renderDivider(divider)
        }
    }

    private fun renderShopIcon(shopLogoUrl : String?) {
        if(shopLogoUrl.isNullOrBlank()){
            shopIcon.hide()
        }else{
            shopIcon.show()
            shopIcon.loadImage(shopLogoUrl)
        }
    }

    private fun renderDivider(isDivider : Boolean){
        if(isDivider) divider.show() else divider.hide()
    }

    private fun getFormattedWeight(weight: Double, qty: Int): String {
        val weighTotalFormatted: String
        var weightTotal = weight * qty
        if (weightTotal >= KILOGRAM_DIVIDER) {
            var bigDecimal = BigDecimal(weightTotal / KILOGRAM_DIVIDER)
            bigDecimal = bigDecimal.setScale(
                DIGIT_AFTER_COMMA,
                BigDecimal.ROUND_HALF_UP
            )
            weightTotal = bigDecimal.toDouble()
            weighTotalFormatted =
                "$weightTotal $LABEL_KILOGRAM"
        } else {
            weighTotalFormatted = weightTotal.toInt()
                .toString() + LABEL_GRAM
        }
        return weighTotalFormatted.replace(".0 ", "")
            .replace("  ", " ")
    }
}