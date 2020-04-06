package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_topchat_product_card.view.*

open class TopchatProductAttachmentViewHolder(
        itemView: View?,
        private val listener: ProductAttachmentListener
) : BaseChatViewHolder<ProductAttachmentViewModel>(itemView) {

    private var wishListBtn: UnifyButton? = null
    private var cardContainer: SingleProductAttachmentContainer? = null
    private var emptyStock: Label? = null

    private val white = "#ffffff"
    private val white2 = "#fff"
    private val labelEmptyStockColor = "#80000000"

    override fun alwaysShowTime(): Boolean = true

    override fun bind(product: ProductAttachmentViewModel?) {
        if (product == null) return
        super.bind(product)
        bindView()
        bindLayoutGravity(product)
        bindProductClick(product)
        bindImage(product)
        bindImageClick(product)
        bindName(product)
        bindVariant(product)
        bindCampaign(product)
        bindPrice(product)
        bindFreeShipping(product)
        bindFooter(product)
        bindEmptyStockLabel(product)
        bindChatReadStatus(product)
        listener.trackSeenProduct(product)
    }

    private fun bindView() {
        wishListBtn = itemView.findViewById(R.id.tv_wishlist)
        cardContainer = itemView.findViewById(R.id.containerProductAttachment)
        emptyStock = itemView.findViewById(R.id.lb_empty_stock)
    }

    private fun bindLayoutGravity(product: ProductAttachmentViewModel) {
        if (product.isSender) {
            cardContainer?.gravityRight()
        } else {
            cardContainer?.gravityLeft()
        }
    }

    private fun bindProductClick(product: ProductAttachmentViewModel) {
        itemView.setOnClickListener { listener.onProductClicked(product) }
    }

    private fun bindImage(product: ProductAttachmentViewModel) {
        ImageHandler.loadImageRounded2(
                itemView.context,
                itemView.iv_thumbnail,
                product.productImage,
                8f.toPx()
        )
    }

    private fun bindImageClick(product: ProductAttachmentViewModel) {
        itemView.iv_thumbnail?.setOnClickListener {
            listener.trackClickProductThumbnail(product)
            it.context.startActivity(
                    ImagePreviewActivity.getCallingIntent(
                            it.context,
                            ArrayList(product.images),
                            null,
                            0
                    )
            )
        }
    }

    private fun bindName(product: ProductAttachmentViewModel) {
        itemView.tv_product_name?.let {
            it.text = product.productName
        }
    }

    private fun bindVariant(product: ProductAttachmentViewModel) {
        if (product.doesNotHaveVariant()) {
            hideVariantLayout()
            return
        }

        showVariantLayout()
        with(itemView) {
            if (product.hasColorVariant()) {
                ll_variant_color?.show()
                val backgroundDrawable = getBackgroundDrawable(product.colorHexVariant)
                iv_variant_color?.background = backgroundDrawable
                tv_variant_color?.text = product.colorVariant
            } else {
                ll_variant_color?.hide()
            }

            if (product.hasSizeVariant()) {
                ll_variant_size?.show()
                tv_variant_size?.text = product.sizeVariant
            } else {
                ll_variant_size?.hide()
            }
        }
    }

    private fun hideVariantLayout() {
        itemView.ll_variant?.hide()
    }

    private fun showVariantLayout() {
        itemView.ll_variant?.show()
    }

    private fun getBackgroundDrawable(hexColor: String): Drawable? {
        val backgroundDrawable = MethodChecker.getDrawable(itemView.context, com.tokopedia.chat_common.R.drawable.circle_color_variant_indicator)
                ?: return null

        if (isWhiteColor(hexColor)) {
            applyStrokeTo(backgroundDrawable)
            return backgroundDrawable
        }

        backgroundDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun applyStrokeTo(backgroundDrawable: Drawable) {
        if (backgroundDrawable is GradientDrawable) {
            val strokeWidth = 1f.toPx()
            backgroundDrawable.setStroke(strokeWidth.toInt(), ContextCompat.getColor(itemView.context, com.tokopedia.chat_common.R.color.grey_300))
        }
    }

    private fun isWhiteColor(hexColor: String): Boolean {
        return hexColor == white || hexColor == white2
    }

    private fun bindCampaign(product: ProductAttachmentViewModel) {
        if (product.hasDiscount) {
            toggleCampaign(View.VISIBLE)
            bindDiscount(product)
            bindDropPrice(product)
        } else {
            toggleCampaign(View.GONE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: ProductAttachmentViewModel) {
        itemView.tv_campaign_discount?.text = "${product.dropPercentage}%"
    }

    private fun bindDropPrice(product: ProductAttachmentViewModel) {
        itemView.tv_campaign_price?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = product.priceBefore
        }
    }

    private fun bindPrice(product: ProductAttachmentViewModel) {
        itemView.tv_price?.text = product.productPrice
    }

    private fun bindFreeShipping(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping()) {
            itemView.iv_free_shipping?.show()
            ImageHandler.loadImageRounded2(itemView.context, itemView.iv_free_shipping, product.getFreeShippingImageUrl())
        } else {
            itemView.iv_free_shipping?.hide()
        }
    }

    private fun bindFooter(product: ProductAttachmentViewModel) {
        if (product.canShowFooter && !GlobalConfig.isSellerApp()) {
            bindBuy(product)
            bindAtc(product)
            bindWishList(product)
        } else {
            hideFooter()
        }
    }

    private fun bindEmptyStockLabel(product: ProductAttachmentViewModel) {
        emptyStock?.apply {
            if (product.hasEmptyStock()) {
                show()
                unlockFeature = true
                setLabelType(labelEmptyStockColor)
            } else {
                hide()
            }
        }
    }

    private fun hideFooter() {
        itemView.tv_buy?.hide()
        itemView.tv_atc?.hide()
        itemView.tv_wishlist?.hide()
    }

    private fun bindBuy(product: ProductAttachmentViewModel) {
        itemView.tv_buy?.let {
            it.show()
            if (product.hasEmptyStock()) {
                it.isEnabled = false
                it.setText(com.tokopedia.chat_common.R.string.action_empty_stock)
            } else {
                it.isEnabled = true
                it.setText(com.tokopedia.chat_common.R.string.action_buy)
                it.setOnClickListener {
                    listener.onClickBuyFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindAtc(product: ProductAttachmentViewModel) {
        itemView.tv_atc?.let {
            if (product.hasEmptyStock()) {
                it.hide()
            } else {
                it.show()
                it.setOnClickListener {
                    listener.onClickATCFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindWishList(product: ProductAttachmentViewModel) {
        if (product.hasEmptyStock()) {
            wishListBtn?.show()
            wishListBtn?.setOnClickListener {
                listener.onClickAddToWishList(product) {
                    product.wishList = true
                }
            }
        } else {
            wishListBtn?.hide()
        }
    }

    private fun toggleCampaign(visibility: Int) {
        with(itemView) {
            tv_campaign_discount?.visibility = visibility
            tv_campaign_price?.visibility = visibility
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_attachment
    }
}