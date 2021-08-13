package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.ColorDrawableGenerator
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.toPx

class ProductPreviewViewHolder(itemView: View, attachmentItemPreviewListener: AttachmentItemPreviewListener)
    : AttachmentPreviewViewHolder<SendableProductPreview>(itemView, attachmentItemPreviewListener) {

    private val productImage = itemView.findViewById<ImageView>(R.id.iv_product)
    private val productName = itemView.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = itemView.findViewById<TextView>(R.id.tv_product_price)
    private val container = itemView.findViewById<ConstraintLayout>(R.id.cl_container)

    private val productVariantContainer = itemView.findViewById<LinearLayout>(R.id.ll_variant)
    private val productColorVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_color)
    private val productColorVariantHex = itemView.findViewById<ImageView>(R.id.iv_variant_color)
    private val productColorVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_color)
    private val productSizeVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_size)
    private val productSizeVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_size)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            view = container,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_N0,
            topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            topRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            shadowColor = R.color.topchat_dms_black_12,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_2,
            shadowGravity = Gravity.CENTER,
            useViewPadding = true
    )

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.findViewById(R.id.iv_close)
    }

    override fun bind(model: SendableProductPreview) {
        super.bind(model)
        bindBackground()
        bindImageThumbnail(model)
        bindProductName(model)
        bindProductPrice(model)
        bindProductVariant(model)
    }

    private fun bindProductName(model: SendableProductPreview) {
        productName?.text = model.productPreview.name
    }

    private fun bindProductPrice(model: SendableProductPreview) {
        productPrice?.text = model.productPreview.price
    }

    private fun bindProductVariant(model: SendableProductPreview) {
        val productPreview = model.productPreview
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
    }

    private fun bindImageThumbnail(model: SendableProductPreview) {
        ImageHandler.loadImageRounded(
                productImage?.context,
                productImage, model.productPreview.imageUrl,
                6.toPx().toFloat()
        )
    }

    private fun bindBackground() {
        container?.background = bg
    }

    companion object {
        val LAYOUT = R.layout.item_product_preview
    }
}