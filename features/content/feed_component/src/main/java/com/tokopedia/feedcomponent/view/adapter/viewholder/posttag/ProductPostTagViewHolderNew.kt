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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val RATING_FORMAT = 20.0

class ProductPostTagViewHolderNew(
    val mainView: View,
    val listener: ProductItemInfoBottomSheet.Listener
) : AbstractViewHolder<ProductPostTagModelNew>(mainView) {

    private val productLayout: FrameLayout
    private val productImage: ImageUnify
    private val productPrice: Typography
    private val productName: Typography
    private val discountlayout: LinearLayout
    private val addToWishlistBtn: FrameLayout
    private val addToCartBtn: UnifyButton
    private val stockProgressBar: ProgressBarUnify
    private val productTag: Typography
    private val productNameSection: LinearLayout
    private val rating: Typography
    private val label: Label
    private val soldInfo: Typography
    private val freeShipping: ImageView
    private val divider: View
    private val stockBarLayout: View
    private val stockText: Typography
    private val star: IconUnify
    private val menuBtn: IconUnify
    private val card: CardUnify
    private val addToWishlistBtnIcon: IconUnify

    init {
        productLayout = itemView.findViewById(R.id.productLayout)
        productImage = itemView.findViewById(R.id.productImage)
        productPrice = itemView.findViewById(R.id.productPrice)
        discountlayout = itemView.findViewById(R.id.discount_layout)
        productTag = itemView.findViewById(R.id.productTag)
        productNameSection = itemView.findViewById(R.id.productNameSection)
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
        addToWishlistBtnIcon = itemView.findViewById(R.id.image_add_to_wishlist)
    }

    override fun bind(item: ProductPostTagModelNew) {
        label.showWithCondition(item.isDiscount && item.isUpcoming.not())
        productTag.showWithCondition(item.isDiscount || item.isUpcoming)
        discountlayout.showWithCondition(item.isDiscount || item.isUpcoming)
        if (item.isUpcoming) {
            productPrice.text = item.product.priceMaskedFmt
            productTag.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.priceFmt
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
        menuBtn.showWithCondition((item.isFlashSaleToko || item.isRilisanSpl).not())
        rating.text = String.format("%.1f", item.rating.toDouble() / RATING_FORMAT)
        val soldInfoText = getString(contentcommonR.string.feed_common_terjual) + " " + item.totalSold.toString()
        soldInfo.text = soldInfoText
        star.showWithCondition(item.rating != 0)
        divider.showWithCondition(item.rating != 0 && item.totalSold != 0)
        rating.showWithCondition(item.rating != 0)
        soldInfo.showWithCondition(item.totalSold != 0)

        addToWishlistBtn.showWithCondition(item.isUpcoming || item.isOngoing)
        addToCartBtn.showWithCondition(item.isUpcoming || item.isOngoing)
        val isUpcomingAndRilisanSpecial = item.isUpcoming && item.isRilisanSpl
        addToCartBtn.isEnabled = item.product.cartable && !isUpcomingAndRilisanSpecial

        addToCartBtn.setOnClickListener { listener.onAddToCartButtonClicked(item) }
        setWislistIconStateColor(item.isWishlisted)
        addToWishlistBtn.setOnClickListener {
            listener.onAddToWishlistButtonClicked(item, adapterPosition)
        }

        if (isUpcomingAndRilisanSpecial) {
            addToCartBtn.apply {
                isEnabled = false
                text =
                    getString(contentcommonR.string.btn_add_to_cart_text_disabled)
            }
        }

        if (item.isOngoing) {
            setGradientColorForProgressBar(item)
        }
    }

    private fun setGradientColorForProgressBar(item: ProductPostTagModelNew ){
        val progressBarColor: IntArray = intArrayOf(
            ContextCompat.getColor(itemView.context, contentcommonR.color.content_dms_asgc_progress_0_color),
            ContextCompat.getColor(itemView.context, contentcommonR.color.content_dms_asgc_progress_100_color)
        )

        val value = (item.product.stockSoldPercentage).roundToInt()
        stockProgressBar.setValue(value, true)
        stockProgressBar.progressBarColor = progressBarColor
        stockText.text = item.product.stockWording
        stockBarLayout.visible()
    }

    private fun setWislistIconStateColor(isWishlisted: Boolean) {
        itemView.run {
            if (isWishlisted) {
                val colorRed =
                    MethodChecker.getColor(
                        context,
                        unifyprinciplesR.color.Unify_RN500
                    )
                addToWishlistBtnIcon.setImage(IconUnify.HEART_FILLED, colorRed, colorRed)
            }else{ val colorRed =
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN500
                )
                addToWishlistBtnIcon.setImage(IconUnify.HEART, colorRed, colorRed)
            }
        }
    }


    private fun getItemClickNavigationListener(
        listener: ProductItemInfoBottomSheet.Listener,
        positionInFeed: Int,
        item: FeedXProduct, itemPosition: Int,
        mediaType: String
    ): View.OnClickListener {
        return View.OnClickListener {
            listener.onTaggedProductCardClicked(
                positionInFeed,
                item.appLink,
                item,
                itemPosition + 1,
                mediaType = mediaType
            )
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
