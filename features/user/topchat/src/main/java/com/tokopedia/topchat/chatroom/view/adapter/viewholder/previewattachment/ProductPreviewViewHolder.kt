package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.toPx

class ProductPreviewViewHolder(itemView: View, attachmentItemPreviewListener: AttachmentItemPreviewListener)
    : AttachmentPreviewViewHolder<SendableProductPreview>(itemView, attachmentItemPreviewListener) {

    private val productImage = itemView.findViewById<ImageView>(R.id.iv_product)
    private val productName = itemView.findViewById<TextView>(R.id.tv_product_name)
    private val container = itemView.findViewById<ConstraintLayout>(R.id.cl_container)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            container,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_6,
            R.dimen.dp_topchat_6,
            Gravity.CENTER
    )

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.findViewById(R.id.iv_close)
    }

    override fun bind(model: SendableProductPreview) {
        super.bind(model)
        bindBackground()
        bindImageThumbnail(model)
        bindProductName(model)
    }

    private fun bindProductName(model: SendableProductPreview) {
        productName?.text = model.productPreview.name
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