package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.topchat.common.util.ViewUtil.ellipsizeLongText
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx

class ProductPreviewViewHolder(
    itemView: View, attachmentItemPreviewListener: AttachmentItemPreviewListener
) : AttachmentPreviewViewHolder<TopchatProductAttachmentPreviewUiModel>(
    itemView, attachmentItemPreviewListener
) {

    private val productImage = itemView.findViewById<ImageView>(R.id.iv_product)
    private val productName = itemView.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = itemView.findViewById<TextView>(R.id.tv_product_price)
    private val container = itemView.findViewById<ConstraintLayout>(R.id.cl_container)

    private val productVariantContainer = itemView.findViewById<LinearLayout>(R.id.ll_variant)
    private val productColorVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_color)
    private val productColorVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_color)
    private val productSizeVariant = itemView.findViewById<LinearLayout>(R.id.ll_variant_size)
    private val productSizeVariantValue = itemView.findViewById<TextView>(R.id.tv_variant_size)
    private val loader = itemView.findViewById<LoaderUnify>(R.id.lu_product_preview)
    private val retry = itemView.findViewById<IconUnify>(R.id.iu_retry_product_preview)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            view = container,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
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

    override fun bind(model: TopchatProductAttachmentPreviewUiModel, payloads: List<Any>) {
        if (payloads.isEmpty()) return
        if (payloads.first() == Payload.REBIND) {
            bind(model)
        }
    }

    override fun bind(model: TopchatProductAttachmentPreviewUiModel) {
        super.bind(model)
        bindBackground()
        when {
            model.isLoading -> bindLoadingState(model)
            model.isError -> bindErrorState(model)
            else -> bindSuccessState(model)
        }
    }

    private fun bindSuccessState(model: TopchatProductAttachmentPreviewUiModel) {
        showLoading(false)
        showError(false)
        bindImageThumbnail(model)
        bindProductName(model)
        bindProductPrice(model)
        bindProductVariant(model)
    }

    private fun bindLoadingState(model: TopchatProductAttachmentPreviewUiModel) {
        // TODO: implement based on design
        showLoading(true)
        showError(false)
        hideProductComponents()
    }

    private fun bindErrorState(model: TopchatProductAttachmentPreviewUiModel) {
        // TODO: implement based on design
        showLoading(false)
        showError(true)
        hideProductComponents()
        retry?.setOnClickListener {
            attachmentItemPreviewListener.retryLoadCurrentAttachment()
        }
    }

    private fun hideProductComponents() {
        productName?.hide()
        productPrice?.hide()
        productVariantContainer?.hide()
        productImage?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        loader?.showWithCondition(isLoading)
    }

    private fun showError(isError: Boolean) {
        retry?.showWithCondition(isError)
    }

    private fun bindProductName(model: TopchatProductAttachmentPreviewUiModel) {
        productName?.show()
        productName?.text = model.productName
    }

    private fun bindProductPrice(model: TopchatProductAttachmentPreviewUiModel) {
        productPrice?.show()
        productPrice?.text = model.productPrice
    }

    private fun bindProductVariant(model: TopchatProductAttachmentPreviewUiModel) {
        productVariantContainer?.showWithCondition(model.hasVariant())
        if (model.hasColorVariant()) {
            productColorVariant.show()
            productColorVariantValue?.text = ellipsizeLongText(
                model.colorVariant, MAX_VARIANT_LABEL_CHAR)
        } else {
            productColorVariant?.hide()
        }

        val productHasSizeVariant = model.hasSizeVariant()
        productSizeVariant?.shouldShowWithAction(productHasSizeVariant) {
            productSizeVariantValue?.text = ellipsizeLongText(
                model.sizeVariant, MAX_VARIANT_LABEL_CHAR)
        }
    }

    private fun bindImageThumbnail(model: TopchatProductAttachmentPreviewUiModel) {
        productImage?.show()
        productImage?.loadImageRounded(model.productImage, 6.toPx().toFloat())
    }

    private fun bindBackground() {
        container?.background = bg
    }

    companion object {
        private const val MAX_VARIANT_LABEL_CHAR = 5
        val LAYOUT = R.layout.item_product_preview
    }
}