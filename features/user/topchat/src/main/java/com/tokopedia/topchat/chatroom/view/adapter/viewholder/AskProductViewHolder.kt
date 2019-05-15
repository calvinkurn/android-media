package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
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

    private val closeButton = itemView?.findViewById<ImageView>(R.id.iv_close)

    init {
        closeButton?.setOnClickListener {

        }
    }

    fun bind(askedProduct: AskedProduct) {
        ImageHandler.loadImageRounded(productImage?.context, productImage, askedProduct.imageUrl, toDp(3))
        productName?.text = askedProduct.name
        productPrice?.text = askedProduct.price

        if (askedProduct.doesNotHaveVariant()) {
            hideVariantLayout()
            return
        }

        if (askedProduct.hasColorVariant()) {
            val backgroundDrawable = getBackgroundDrawable(askedProduct.colorHexVariant)
            productColorVariantHex?.background = backgroundDrawable
            productColorVariantValue?.text = askedProduct.colorVariant
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

    private fun toDp(number: Int): Float {
        return number * Resources.getSystem().displayMetrics.density + 0.5f
    }

    private fun getBackgroundDrawable(hexColor: String): Drawable? {
        val backgroundDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator)
        backgroundDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

}