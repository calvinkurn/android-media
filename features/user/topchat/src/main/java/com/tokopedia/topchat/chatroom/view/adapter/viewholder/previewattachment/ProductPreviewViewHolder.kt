package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview

class ProductPreviewViewHolder(itemView: View, attachmentItemPreviewListener: AttachmentItemPreviewListener)
    : AttachmentPreviewViewHolder<SendableProductPreview>(itemView, attachmentItemPreviewListener) {

    private val productImage = itemView.findViewById<ImageView>(R.id.iv_product)
    private val productName = itemView.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = itemView.findViewById<TextView>(R.id.tv_product_price)

    private val productVariantContainer = itemView.findViewById<LinearLayout>(R.id.ll_variant)
    private val productColorVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_color)
    private val productColorVariantHex = itemView.findViewById<ImageView>(R.id.iv_variant_color)
    private val productColorVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_color)
    private val productSizeVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_size)
    private val productSizeVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_size)

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.findViewById(R.id.iv_close)
    }

    override fun bind(model: SendableProductPreview, position: Int) {
        super.bind(model, position)
        val productPreview = model.productPreview

        ImageHandler.loadImageRounded(productImage?.context, productImage, productPreview.imageUrl, toDp(3))
        productName?.text = productPreview.name
        productPrice?.text = productPreview.price

        productVariantContainer?.showWithCondition(model.hasVariant())

        if (model.hasColorVariant()) {
            productColorVariant.show()
            val backgroundDrawable = getBackgroundDrawable(productPreview.colorHexVariant)
            productColorVariantHex?.background = backgroundDrawable
            productColorVariantValue?.text = productPreview.colorVariant
        } else {
            productColorVariant?.hide()
        }

        val productHasSizeVariant = model.hasSizeVariant()
        productSizeVariant?.shouldShowWithAction(productHasSizeVariant) {
            productSizeVariantValue?.text = productPreview.sizeVariant
        }
    }

    private fun hideVariantLayout() {
        productVariantContainer?.hide()
    }

    private fun getBackgroundDrawable(hexColor: String): Drawable? {
        val backgroundDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator)

        if (isWhiteColor(hexColor)) {
            applyStrokeTo(backgroundDrawable)
            return backgroundDrawable
        }

        backgroundDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun isWhiteColor(hexColor: String): Boolean {
        return hexColor == "#ffffff" || hexColor == "#fff"
    }

    private fun applyStrokeTo(backgroundDrawable: Drawable?) {
        if (backgroundDrawable is GradientDrawable) {
            val strokeWidth = toDp(1)
            backgroundDrawable.setStroke(strokeWidth.toInt(), ContextCompat.getColor(itemView.context, R.color.grey_300))
        }
    }

    private fun toDp(number: Int): Float {
        return number * Resources.getSystem().displayMetrics.density + 0.5f
    }

    companion object {
        val LAYOUT = R.layout.item_product_preview
    }
}