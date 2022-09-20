package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

private const val RATING_FORMAT = 20.0

class ProductPostTagViewHolderNew(
    val mainView: View,
    val listener: ProductItemInfoBottomSheet.Listener
) : AbstractViewHolder<ProductPostTagViewModelNew>(mainView) {

    private lateinit var productLayout: FrameLayout
    private lateinit var productImage: ImageUnify
    private lateinit var productPrice: Typography
    private lateinit var productName: Typography
    private lateinit var discountlayout: LinearLayout
    private lateinit var addToWishlistBtn: FrameLayout
    private lateinit var addToCartBtn: UnifyButton
    private lateinit var stockProgressBar: ProgressBarUnify
    private lateinit var productTag: Typography
    private lateinit var productNameSection: LinearLayout
    private lateinit var rating: Typography
    private lateinit var label: Label
    private lateinit var soldInfo: Typography
    private lateinit var freeShipping: ImageView
    private lateinit var divider: View
    private lateinit var stockBarLayout: View
    private lateinit var stockText: Typography
    private lateinit var star: IconUnify
    private lateinit var menuBtn: IconUnify
    private lateinit var card: CardUnify

    override fun bind(item: ProductPostTagViewModelNew) {
        productLayout = itemView.findViewById(R.id.productLayout)
        productImage = itemView.findViewById(R.id.productImage)
        productPrice = itemView.findViewById(R.id.productPrice)
        discountlayout = itemView.findViewById(R.id.discount_layout)
        productTag = itemView.findViewById(R.id.productTag)
        productNameSection = itemView.findViewById(R.id.productNameSection)
        productName = itemView.findViewById(R.id.productName)
        productName = itemView.findViewById(R.id.productName)
        rating = itemView.findViewById(R.id.rating)
        stockText = itemView.findViewById(R.id.stock_text)
        stockBarLayout = itemView.findViewById(R.id.product_stock_bar_layout)
        label = itemView.findViewById(R.id.discountLabel)
        soldInfo = itemView.findViewById(R.id.soldInfo)
        freeShipping = itemView.findViewById(R.id.freeShipping)
        divider = itemView.findViewById(R.id.divider)
        star = itemView.findViewById(R.id.star)
        menuBtn = itemView.findViewById(R.id.menu)
        card = itemView.findViewById(R.id.container)
        addToCartBtn = itemView.findViewById(R.id.button_add_to_cart)
        stockProgressBar = itemView.findViewById(R.id.ongoing_progress_bar)
        addToWishlistBtn = itemView.findViewById(R.id.button_add_to_wishlist)
        label.showWithCondition(item.isDiscount && item.isUpcoming.not())
        productTag.showWithCondition(item.isDiscount)
        discountlayout.showWithCondition(item.isDiscount)
        if (item.isUpcoming) {
            productPrice.text = item.product.priceMaskedFmt
            productTag.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.originalPriceFmt
            }
        } else {
            if (item.isDiscount) {
                productTag.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = item.originalPriceFmt
                }
                label.text = item.discountFmt
                productPrice.text = item.priceDiscountFmt

            } else {
                productPrice.text = item.priceFmt
            }
        }

        freeShipping.showWithCondition(item.isFreeShipping)
        if (item.isFreeShipping) {
            freeShipping.loadImage(item.freeShippingURL)
        }
        productImage.setImageUrl(item.imgUrl)
        productName.text = item.text

        card.setOnClickListener (
            getItemClickNavigationListener(
                    listener,
                    item.positionInFeed,
                    item.product,
                    adapterPosition,
                    mediaType = item.mediaType
            )
        )
        menuBtn.setOnClickListener {
            listener.onBottomSheetThreeDotsClicked(item, mainView.context)
        }
        rating.text = String.format("%.1f", item.rating.toDouble() / RATING_FORMAT)
        val soldInfoText = getString(R.string.feed_common_terjual) + " " + item.totalSold.toString()
        soldInfo.text = soldInfoText
        star.showWithCondition(item.rating != 0)
        divider.showWithCondition(item.rating != 0 && item.totalSold != 0)
        rating.showWithCondition(item.rating != 0)
        soldInfo.showWithCondition(item.totalSold != 0)

        addToWishlistBtn.showWithCondition(item.isUpcoming || item.isOngoing)
        addToCartBtn.showWithCondition(item.isUpcoming || item.isOngoing)
        addToCartBtn.isEnabled = item.product.cartable
        val isUpcomingAndRilisanSpecial = item.isUpcoming && item.isRilisanSpl
        addToCartBtn.isEnabled = item.product.cartable && !isUpcomingAndRilisanSpecial

        addToCartBtn.setOnClickListener { listener.onAddToCartButtonClicked(item) }
        addToWishlistBtn.setOnClickListener { listener.onAddToWishlistButtonClicked(item) }

        if (isUpcomingAndRilisanSpecial) {
            addToCartBtn.apply {
                isEnabled = false
                text =
                    getString(R.string.btn_add_to_cart_text_disabled)
            }
        }

        if (item.isOngoing) {
            setGradientColorForProgressBar(item)
        }
    }

    private fun setGradientColorForProgressBar(item: ProductPostTagViewModelNew ){
        val progressBarColor: IntArray = intArrayOf(
            ContextCompat.getColor(itemView.context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_0_color),
            ContextCompat.getColor(itemView.context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_100_color)
        )

        val value = (((item.product.stockSoldPercentage) * 100) / 100).roundToInt()
        stockProgressBar.setValue(value, true)
        stockProgressBar.progressBarColor = progressBarColor
        stockText.text = item.product.stockWording
        stockBarLayout.visible()
    }


    private fun getItemClickNavigationListener(
        listener: ProductItemInfoBottomSheet.Listener,
        positionInFeed: Int,
        item: FeedXProduct, itemPosition: Int,
        mediaType: String
    ): View.OnClickListener {
        return View.OnClickListener {
            listener.onTaggedProductCardClicked(positionInFeed, item.appLink, item, itemPosition+1, mediaType = mediaType)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_producttag_list_new

        fun create(
            parent: ViewGroup,
            listener: ProductItemInfoBottomSheet.Listener
        ) = ProductPostTagViewHolderNew(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_producttag_list_new,
                    parent,
                    false,
                ),
            listener
        )
    }
}