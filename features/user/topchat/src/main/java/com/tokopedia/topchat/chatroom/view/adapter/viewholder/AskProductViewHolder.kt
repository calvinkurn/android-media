package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.AskedProduct

class AskProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val productImage = itemView?.findViewById<ImageView>(R.id.iv_product)
    private val productName = itemView?.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = itemView?.findViewById<TextView>(R.id.tv_product_price)

    private val productVariantContainer = itemView?.findViewById<LinearLayout>(R.id.ll_variant)
    private val productColorVariant = itemView?.findViewById<LinearLayout>(R.id.ll_variant_color)
    private val productColorVariantHex = itemView?.findViewById<ImageView>(R.id.iv_variant_color)
    private val productColorVariantValue = itemView?.findViewById<TextView>(R.id.tv_variant_color)
    private val productSizeVariant = itemView?.findViewById<LinearLayout>(R.id.ll_variant_size)
    private val productSizeVariantValue = itemView?.findViewById<TextView>(R.id.tv_variant_size)

    fun bind(askedProduct: AskedProduct) {
        ImageHandler.loadImageWithoutPlaceholder(productImage, askedProduct.imageUrl)
        productName?.text = askedProduct.name
        productPrice?.text = askedProduct.price

        if (askedProduct.doesNotHaveVariant()) {
            hideVariantLayout()
            return
        }

        if (askedProduct.hasColorVariant()) {
            productColorVariantValue?.text = askedProduct.colorVariant
            productColorVariantHex?.setBackgroundColor(Color.parseColor(askedProduct.colorHexVariant))
        } else {
            productColorVariant?.visibility = View.GONE
        }

        if (askedProduct.hasSizeVariant()) {
            productSizeVariantValue?.text = askedProduct.sizeVariant
        } else {
            productSizeVariant?.visibility = View.GONE
        }
    }

    private fun hideVariantLayout() {
        productVariantContainer?.visibility = View.GONE
    }

}