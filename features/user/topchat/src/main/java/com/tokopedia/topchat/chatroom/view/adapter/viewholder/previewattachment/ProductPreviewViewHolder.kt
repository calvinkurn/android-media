package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.ColorDrawableGenerator
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.unifycomponents.toPx

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

    override fun bind(model: SendableProductPreview) {
        val productPreview = model.productPreview

        ImageHandler.loadImageRounded(
                productImage?.context, productImage, productPreview.imageUrl, 3.toPx().toFloat()
        )
        productName?.text = productPreview.name
        productPrice?.text = productPreview.price

        productVariantContainer?.showWithCondition(model.hasVariant())

        if (model.hasColorVariant()) {
            productColorVariant.show()
            val backgroundDrawable = ColorDrawableGenerator.generate(
                    itemView.context, productPreview.colorHexVariant
            )
            productColorVariantHex?.background = backgroundDrawable
            productColorVariantValue?.text = productPreview.colorVariant
        } else {
            productColorVariant?.hide()
        }

        val productHasSizeVariant = model.hasSizeVariant()
        productSizeVariant?.shouldShowWithAction(productHasSizeVariant) {
            productSizeVariantValue?.text = productPreview.sizeVariant
        }
        super.bind(model)
    }

    companion object {
        val LAYOUT = R.layout.item_product_preview
    }
}