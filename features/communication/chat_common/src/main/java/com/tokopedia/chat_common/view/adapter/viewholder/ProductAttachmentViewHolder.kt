package com.tokopedia.chat_common.view.adapter.viewholder

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp

/**
 * @author by nisie on 5/14/18.
 */
open class ProductAttachmentViewHolder(
    itemView: View?,
    private var viewListener: ProductAttachmentListener?
) : BaseChatViewHolder<ProductAttachmentUiModel>(itemView) {

    private var progressBarSendImage: View? = null
    private var chatStatus: ImageView? = null
    private var chatBalloon: View? = null
    private var thumbnailsImage: ImageView? = null
    private var tvBuy: UnifyButton? = null
    private var ivATC: ImageView? = null
    private var ivWishList: ImageView? = null
    private var footerLayout: View? = null
    private var freeShipping: ImageView? = null
    private var productVariantContainer: LinearLayout? = null
    private var productColorVariant: LinearLayout? = null
    private var productColorVariantHex: ImageView? = null
    private var productColorVariantValue: TextView? = null
    private var productSizeVariant: LinearLayout? = null
    private var productSizeVariantValue: TextView? = null
    private var timestampContainer: LinearLayout? = null
    private var context: Context? = null

    init {
        context = itemView?.context
        chatStatus = itemView?.findViewById(R.id.chat_status)
        progressBarSendImage = itemView?.findViewById(R.id.progress_bar)
        chatBalloon = itemView?.findViewById(R.id.attach_product_chat_container)
        freeShipping = itemView?.findViewById(R.id.iv_free_shipping)
        tvBuy = chatBalloon?.findViewById(R.id.tv_buy)
        ivATC = chatBalloon?.findViewById(R.id.ic_add_to_cart)
        ivWishList = chatBalloon?.findViewById(R.id.ic_add_to_wishlist)
        footerLayout = chatBalloon?.findViewById(R.id.footer_layout)
        productVariantContainer = itemView?.findViewById(R.id.ll_variant)
        productColorVariant = itemView?.findViewById(R.id.ll_variant_color)
        productColorVariantHex = itemView?.findViewById(R.id.iv_variant_color)
        productColorVariantValue = itemView?.findViewById(R.id.tv_variant_color)
        productSizeVariant = itemView?.findViewById(R.id.ll_variant_size)
        productSizeVariantValue = itemView?.findViewById(R.id.tv_variant_size)
        timestampContainer = itemView?.findViewById(R.id.ll_timestamp)
    }

    override fun bind(uiModel: ProductAttachmentUiModel) {
        super.bind(uiModel)
        uiModel.let {
            prerequisiteUISetup(it)
            setupProductUI(it, chatBalloon)
            setupFreeShipping(it)
            setupChatBubbleAlignment(chatBalloon, it)
            setupVariantLayout(it)
            setupIfEmptyStock(it)
            viewListener?.trackSeenProduct(it)
        }
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    override val dateId: Int
        get() = R.id.tvDate

    private fun setupIfEmptyStock(element: ProductAttachmentUiModel) {
        if (element.hasEmptyStock()) {
            tvBuy?.isEnabled = false
            tvBuy?.setText(R.string.action_empty_stock)
            ivATC?.setImageDrawable(
                MethodChecker.getDrawable(itemView.context, R.drawable.ic_cart_greyscale_disabled)
            )
        } else {
            tvBuy?.isEnabled = true
            tvBuy?.setText(R.string.action_buy)
            ivATC?.setImageDrawable(
                MethodChecker.getDrawable(
                    itemView.context,
                    com.tokopedia.resources.common.R.drawable.ic_cart_grayscale_20
                )
            )
        }
    }

    private fun setupVariantLayout(element: ProductAttachmentUiModel) {
        if (element.doesNotHaveVariant()) {
            hideVariantLayout()
        } else {
            showVariantLayout()
        }
        if (element.hasColorVariant()) {
            productColorVariant?.visibility = View.VISIBLE
            val backgroundDrawable = getBackgroundDrawable(element.colorHexVariant)
            productColorVariantHex?.background = backgroundDrawable
            productColorVariantValue?.text = element.colorVariant
        } else {
            productColorVariant?.visibility = View.GONE
        }
        if (element.hasSizeVariant()) {
            productSizeVariant?.visibility = View.VISIBLE
            productSizeVariantValue?.text = element.sizeVariant
        } else {
            productSizeVariant?.visibility = View.GONE
        }
    }

    private fun hideVariantLayout() {
        productVariantContainer?.visibility = View.GONE
    }

    private fun showVariantLayout() {
        productVariantContainer?.visibility = View.VISIBLE
    }

    private fun getBackgroundDrawable(hexColor: String): Drawable? {
        val backgroundDrawable = MethodChecker.getDrawable(
            itemView.context,
            R.drawable.topchat_circle_color_variant_indicator
        )
            ?: return null
        if (isWhiteColor(hexColor)) {
            applyStrokeTo(backgroundDrawable)
            return backgroundDrawable
        }
        backgroundDrawable.colorFilter =
            PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun applyStrokeTo(backgroundDrawable: Drawable) {
        if (backgroundDrawable is GradientDrawable) {
            val strokeWidth = 1.toDp().toFloat()
            backgroundDrawable.setStroke(
                strokeWidth.toInt(),
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN200
                )
            )
        }
    }

    private fun isWhiteColor(hexColor: String): Boolean {
        return hexColor == context?.getString(R.string.white_color_hex) || hexColor == context?.getString(
            R.string.white_color_hex_simpler
        )
    }

    private fun setupChatBubbleAlignment(
        productContainerView: View?,
        element: ProductAttachmentUiModel
    ) {
        if (element.isSender) {
            setChatRight(productContainerView, element)
        } else {
            setChatLeft(productContainerView)
        }
    }

    private fun setChatLeft(productContainerView: View?) {
        productContainerView?.background =
            MethodChecker.getDrawable(
                productContainerView?.context,
                R.drawable.bg_shadow_attach_product
            )
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, productContainerView)
        chatStatus?.visibility = View.GONE
    }

    private fun setChatRight(productContainerView: View?, element: ProductAttachmentUiModel) {
        productContainerView?.background = MethodChecker.getDrawable(
            productContainerView?.context,
            R.drawable.bg_shadow_attach_product
        )
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, productContainerView)
        bindChatReadStatus(element)
    }

    override fun bindChatReadStatus(element: SendableUiModel) {
        super.bindChatReadStatus(element)
        if (alwaysShowTime()) {
            timestampContainer?.visibility = View.VISIBLE
        } else {
            timestampContainer?.visibility = View.GONE
        }
    }

    protected fun prerequisiteUISetup(element: ProductAttachmentUiModel?) {
        progressBarSendImage?.visibility = View.GONE
        chatBalloon?.setOnClickListener {
            element?.let {
                viewListener?.onProductClicked(it)
            }
        }
    }

    private fun setAlignParent(alignment: Int, view: View?) {
        val params = view?.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        view.layoutParams = params
    }

    private fun setupProductUI(element: ProductAttachmentUiModel, productContainer: View?) {
        setUIValue(
            productContainer, R.id.attach_product_chat_image, element
                .productImage
        )
        setUIValue(productContainer, R.id.attach_product_chat_name, element.productName)
        setUIValue(productContainer, R.id.attach_product_chat_price, element.productPrice)
        setUIDiscount(productContainer, element)
        setFooter(productContainer, element)
    }

    private fun setupFreeShipping(product: ProductAttachmentUiModel) {
        if (product.hasFreeShipping()) {
            freeShipping?.visibility = View.VISIBLE
            ImageHandler.loadImageRounded2(
                itemView.context,
                freeShipping,
                product.getFreeShippingImageUrl()
            )
        }
    }

    private fun setUIDiscount(productContainer: View?, element: ProductAttachmentUiModel) {
        setUIVisibility(productContainer, R.id.discount, element.priceBefore)
        setUIValue(productContainer, R.id.attach_product_chat_price_old, element.priceBefore)
        setUIValue(productContainer, R.id.discount, element.dropPercentage + "%")
        setUIVisibility(productContainer, R.id.discount, element.dropPercentage)
        setUIVisibility(productContainer, R.id.attach_product_chat_price_old, element.priceBefore)
        setStrikeThrough(productContainer, R.id.attach_product_chat_price_old)
    }

    private fun setUIVisibility(productContainer: View?, resourceId: Int, content: String) {
        val destination = productContainer?.findViewById<View>(resourceId)
        if (!TextUtils.isEmpty(content)) {
            destination?.visibility = View.VISIBLE
        } else {
            destination?.visibility = View.GONE
        }
    }

    private fun setFooter(productContainer: View?, element: ProductAttachmentUiModel) {
        if (element.canShowFooter && !GlobalConfig.isSellerApp()) {
            footerLayout?.visibility = View.VISIBLE
            tvBuy?.visibility = View.VISIBLE
            ivATC?.visibility = View.VISIBLE
            ivWishList?.visibility = View.VISIBLE
            tvBuy?.setOnClickListener { v: View? ->
                viewListener?.onClickBuyFromProductAttachment(
                    element
                )
            }
            ivATC?.setOnClickListener { v: View? ->
                if (!element.hasEmptyStock()) {
                    viewListener?.onClickATCFromProductAttachment(element)
                }
            }
            bindWishListView(element)
            bindClickAddToWishList(element)
        } else {
            footerLayout?.visibility = View.GONE
            tvBuy?.visibility = View.GONE
            ivATC?.visibility = View.GONE
            ivWishList?.visibility = View.GONE
        }
    }

    private fun bindWishListView(element: ProductAttachmentUiModel) {
        val loveDrawable = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.ic_attachproduct_wishlist
        )
            ?: return
        loveDrawable.mutate()
        ivWishList?.setImageDrawable(loveDrawable)
        updateWishListIconState(element)
    }

    private fun bindClickAddToWishList(element: ProductAttachmentUiModel) {
        ivWishList?.setOnClickListener { v: View? ->
            if (element.isWishListed()) {
                removeProductFromWishList(element)
            } else {
                addProductToWishList(element)
            }
        }
    }

    private fun removeProductFromWishList(element: ProductAttachmentUiModel) {
        viewListener?.onClickRemoveFromWishList(element.getStringProductId()) {
            onSuccessRemoveFromWishList(element)
            null
        }
    }

    private fun addProductToWishList(element: ProductAttachmentUiModel) {
        viewListener?.onClickAddToWishList(
            element
        ) {
            onSuccessAddToWishList(element)
            null
        }
    }

    private fun onSuccessRemoveFromWishList(element: ProductAttachmentUiModel) {
        element.wishList = false
        updateWishListIconState(element)
    }

    private fun onSuccessAddToWishList(element: ProductAttachmentUiModel) {
        element.wishList = true
        updateWishListIconState(element)
    }

    private fun updateWishListIconState(element: ProductAttachmentUiModel) {
        if (element.isWishListed()) {
            val color = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            )
            ivWishList?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        } else {
            ivWishList?.clearColorFilter()
        }
    }

    private fun setUIValue(productContainer: View?, id: Int, value: String) {
        val destination = productContainer?.findViewById<View>(id)
        if (destination is Label) {
            destination.setLabel(value)
        } else if (destination is TextView) {
            destination.text = value
        } else if (destination is ImageView) {
            ImageHandler.loadImageRounded2(
                destination.getContext(),
                destination,
                value,
                toPx(8f)
            )
            thumbnailsImage = destination
        }
    }

    private fun setStrikeThrough(productContainer: View?, id: Int) {
        val destination = productContainer?.findViewById<View>(id)
        if (destination is TextView) {
            destination.paintFlags =
                destination.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun toPx(value: Float): Float {
        return Resources.getSystem().displayMetrics.density * value
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (thumbnailsImage != null && thumbnailsImage?.context != null) {
            ImageHandler.clearImage(thumbnailsImage)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.attached_product_chat_item
        private const val ROLE_USER = "User"
    }
}