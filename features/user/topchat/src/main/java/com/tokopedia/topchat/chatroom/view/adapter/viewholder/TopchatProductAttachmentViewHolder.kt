package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.common.Constant
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_topchat_product_card.view.*

open class TopchatProductAttachmentViewHolder constructor(
        itemView: View?,
        private val listener: ProductAttachmentListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener
) : BaseChatViewHolder<ProductAttachmentViewModel>(itemView) {

    private var wishListBtn: UnifyButton? = itemView?.findViewById(R.id.tv_wishlist)
    private var cardContainer: SingleProductAttachmentContainer? = itemView?.findViewById(R.id.containerProductAttachment)
    private var label: Label? = itemView?.findViewById(R.id.lb_product_label)
    private var loadView: LoaderUnify? = itemView?.findViewById(R.id.iv_attachment_shimmer)
    private var freeShippingImage: ImageView? = itemView?.findViewById(R.id.iv_free_shipping)
    private var statusContainer: LinearLayout? = itemView?.findViewById(R.id.ll_status_container)
    private var reviewStar: ImageView? = itemView?.findViewById(R.id.iv_review_star)
    private var reviewScore: Typography? = itemView?.findViewById(R.id.tv_review_score)
    private var reviewCount: Typography? = itemView?.findViewById(R.id.tv_review_count)
    private var productName: Typography? = itemView?.findViewById(R.id.tv_product_name)

    private val white = "#ffffff"
    private val white2 = "#fff"
    private val labelEmptyStockColor = "#AD31353B"

    override fun alwaysShowTime(): Boolean = true

    override fun bind(element: ProductAttachmentViewModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bindDeferredAttachment(element)
        }
    }

    private fun bindDeferredAttachment(element: ProductAttachmentViewModel) {
        bind(element)
    }

    override fun bind(product: ProductAttachmentViewModel) {
        super.bind(product)
        bindSyncProduct(product)
        bindLayoutGravity(product)
        if (product.isLoading && !product.isError) {
            bindIsLoading(product)
        } else {
            bindIsLoading(product)
            bindProductClick(product)
            bindImage(product)
            bindImageClick(product)
            bindProductName(product)
            bindVariant(product)
            bindCampaign(product)
            bindPrice(product)
            bindStatusContainer(product)
            bindRating(product)
            bindFreeShipping(product)
            bindFooter(product)
            bindPreOrderLabel(product)
            bindEmptyStockLabel(product)
            bindChatReadStatus(product)
            listener.trackSeenProduct(product)
        }
    }

    private fun bindSyncProduct(product: ProductAttachmentViewModel) {
        if (!product.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[product.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            product.syncError()
        } else {
            product.updateData(attachment.parsedAttributes)
        }
    }

    private fun bindIsLoading(product: ProductAttachmentViewModel) {
        if (product.isLoading) {
            loadView?.show()
        } else {
            loadView?.hide()
        }
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

    private fun bindProductName(product: ProductAttachmentViewModel) {
        val query = searchListener.getSearchQuery()
        val spanText = SpannableString(product.productName)
        if (query.isNotEmpty()) {
            val color = Constant.searchTextBackgroundColor
            val index = spanText.indexOf(query, ignoreCase = true)
            if (index != -1) {
                var lastIndex = index + query.length
                if (lastIndex > spanText.lastIndex) {
                    lastIndex = spanText.lastIndex
                }
                spanText.setSpan(BackgroundColorSpan(color), index, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        productName?.text = spanText
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

    private fun bindStatusContainer(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping() || (product.hasReview() && product.fromBroadcast())) {
            statusContainer?.show()
        } else {
            statusContainer?.hide()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindRating(product: ProductAttachmentViewModel) {
        if (product.hasReview() && !commonListener.isSeller()) {
            reviewScore?.text = product.rating.score.toString()
            reviewCount?.text = "(${product.rating.count})"
            reviewStar?.show()
            reviewScore?.show()
            reviewCount?.show()
        } else {
            reviewStar?.hide()
            reviewScore?.hide()
            reviewCount?.hide()
        }
    }

    private fun bindFreeShipping(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping()) {
            freeShippingImage?.show()
            ImageHandler.loadImageRounded2(itemView.context, freeShippingImage, product.getFreeShippingImageUrl())
        } else {
            freeShippingImage?.hide()
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

    private fun bindPreOrderLabel(product: ProductAttachmentViewModel) {
        label?.apply {
            if (product.isPreOrder) {
                show()
                setText(R.string.title_topchat_pre_order)
                unlockFeature = true
                setLabelType(labelEmptyStockColor)
            } else {
                hide()
            }
        }
    }

    private fun bindEmptyStockLabel(product: ProductAttachmentViewModel) {
        label?.apply {
            if (product.hasEmptyStock()) {
                show()
                setText(R.string.title_topchat_empty_stock)
                unlockFeature = true
                setLabelType(labelEmptyStockColor)
            } else {
                if (!product.isPreOrder) {
                    hide()
                }
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
                if (product.isPreOrder) {
                    it.setText(R.string.title_topchat_pre_order_camel)
                } else {
                    it.setText(com.tokopedia.chat_common.R.string.action_buy)
                }
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