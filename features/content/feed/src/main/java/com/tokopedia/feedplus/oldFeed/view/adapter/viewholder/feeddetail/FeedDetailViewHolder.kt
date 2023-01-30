package com.tokopedia.feedplus.oldFeed.view.adapter.viewholder.feeddetail

import android.graphics.Paint
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailProductModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt
import com.tokopedia.feedcomponent.R as feedComponentR

/**
 * @author by nisie on 5/18/17.
 */

private const val RATING_FORMAT = 20.0


class FeedDetailViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<FeedDetailProductModel>(itemView) {

    private val discountLayout: LinearLayout
    private val productImage: ImageUnify
    private val discountLabel: Label
    private val productPrice: Typography
    private val productName: Typography
    private val productTag: Typography
    private val rating: Typography
    private val soldInfo: Typography
    private val freeShipping: ImageView
    private val divider: View
    private val star: IconUnify
    private val menuBtn: IconUnify
    private val btnAddToCart: UnifyButton
    private val btnAddToWishlist: FrameLayout
    private val btnAddToWishlistIcon: IconUnify
    private val progressBar: ProgressBarUnify
    private val stockProgressBarLayout: View
    private val stockText: Typography
    private val progressBarColor by lazy {
        intArrayOf(
            MethodChecker.getColor(
                itemView.context,
                feedComponentR.color.feed_dms_asgc_progress_0_color
            ),
            MethodChecker.getColor(
                itemView.context,
                feedComponentR.color.feed_dms_asgc_progress_100_color
            )
        )
    }

    init {
        itemView.run {
            productImage = findViewById(R.id.product_image)
            productName = findViewById(R.id.product_name)
            discountLayout = findViewById(R.id.discount_layout)
            discountLabel = findViewById(R.id.discount_label)
            productPrice = findViewById(R.id.product_price)
            productTag = findViewById(R.id.product_tag)
            freeShipping = findViewById(R.id.free_shipping)
            rating = findViewById(R.id.rating)
            menuBtn = findViewById(R.id.menu)
            btnAddToCart = findViewById(R.id.button_add_to_cart_product_detail)
            btnAddToWishlist = findViewById(R.id.button_add_to_wishlist_product_detail)
            btnAddToWishlistIcon = findViewById(R.id.image_add_to_wishlist_product_detail)
            progressBar = findViewById(R.id.ongoing_progress_bar_product_detail)
            stockText = findViewById(R.id.stock_text_product_detail)
            stockProgressBarLayout = findViewById(R.id.product_stock_bar_layout)
            soldInfo = findViewById(R.id.soldInfo)
            divider = findViewById(R.id.divider)
            star = findViewById(R.id.star)
        }
    }

    override fun bind(feedDetailProductModel: FeedDetailProductModel) {
        itemView.run {
            productImage.setImageUrl(feedDetailProductModel.imgUrl)
            productName.text = MethodChecker.fromHtml(feedDetailProductModel.text)

            discountLayout.showWithCondition(feedDetailProductModel.isDiscount || feedDetailProductModel.isUpcoming)
            discountLabel.showWithCondition(feedDetailProductModel.isDiscount)
            if (feedDetailProductModel.isUpcoming) {
                productPrice.text = feedDetailProductModel.product.priceMaskedFmt
                productTag.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = feedDetailProductModel.priceFmt
                    discountLabel.hide()
                }
            } else {
                if (feedDetailProductModel.isDiscount) {
                    discountLabel.text = feedDetailProductModel.discountFmt
                    productTag.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        text = feedDetailProductModel.priceFmt
                    }
                    discountLabel.show()
                    productPrice.text = feedDetailProductModel.priceDiscountFmt
                } else {
                    productPrice.text = feedDetailProductModel.priceFmt
                }
            }

            freeShipping.showWithCondition(feedDetailProductModel.isFreeShipping)
            if (feedDetailProductModel.isFreeShipping) {
                freeShipping.loadImage(feedDetailProductModel.freeShippingURL)
            }

            rating.text = String.format("%.1f", feedDetailProductModel.rating.toDouble() / RATING_FORMAT)
            val soldInfoText = getString(feedComponentR.string.feed_common_terjual) + " " + feedDetailProductModel.totalSold.productThousandFormatted(formatLimit = 1000, isASGCDetailPage = true)

            soldInfo.text = soldInfoText
            star.showWithCondition(feedDetailProductModel.rating != 0)
            divider.showWithCondition(feedDetailProductModel.rating != 0 && feedDetailProductModel.totalSold != 0)
            rating.showWithCondition(feedDetailProductModel.rating != 0)
            soldInfo.showWithCondition(feedDetailProductModel.totalSold != 0)
            menuBtn.showWithCondition(feedDetailProductModel.saleStatus.isBlank())
            btnAddToWishlist.showWithCondition(feedDetailProductModel.isUpcoming || feedDetailProductModel.isOngoing)
            btnAddToCart.showWithCondition(feedDetailProductModel.isUpcoming || feedDetailProductModel.isOngoing)
            val isUpcomingAndRilisanSpecial = feedDetailProductModel.isUpcoming && feedDetailProductModel.isRilisanSpl
            btnAddToCart.isEnabled = feedDetailProductModel.product.cartable
            setWishlistIconStateColor(false)
            if (isUpcomingAndRilisanSpecial){
                btnAddToCart.apply {
                    isEnabled = false
                    text =
                        getString(feedComponentR.string.btn_add_to_cart_text_disabled)
                }
            }
            btnAddToCart.setOnClickListener {
                viewListener.onAddToCartButtonClicked(feedDetailProductModel)
            }
            btnAddToWishlist.setOnClickListener {
                viewListener.onAddToWishlistButtonClicked(feedDetailProductModel, adapterPosition)
            }

            setOnClickListener {
                viewListener.onGoToProductDetail(
                        feedDetailProductModel, adapterPosition)
            }
            menuBtn.setOnClickListener {
                viewListener.onBottomSheetMenuClicked(feedDetailProductModel, itemView.context)
            }
        }
        if (feedDetailProductModel.isOngoing)
            setGradientColorForProgressBar(feedDetailProductModel, itemView)
    }

    override fun bind(element: FeedDetailProductModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() is Int && (payloads.firstOrNull() as Int == PAYLOAD_CLICK_WISHLIST)) {
            element?.isWishlisted = true
            setWishlistIconStateColor(true)
        } else {
            super.bind(element, payloads)
        }
    }

    private fun setGradientColorForProgressBar(item: FeedDetailProductModel, itemView: View) {
        itemView.run {
            progressBar.progressBarColor = progressBarColor
            val value = (item.product.stockSoldPercentage).roundToInt()
            progressBar.setValue(value, true)
            stockText.text = item.product.stockWording
            stockProgressBarLayout.visible()
        }
    }

    private fun setWishlistIconStateColor(isWishlisted: Boolean) {
        itemView.run {
            if (isWishlisted) {
                val colorRed =
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                btnAddToWishlistIcon.setImage(IconUnify.HEART_FILLED, colorRed, colorRed)
                btnAddToWishlist.isEnabled = false
                btnAddToWishlistIcon.isEnabled = false
            } else {
                val colorGrey =
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    )
                btnAddToWishlistIcon.setImage(IconUnify.HEART, colorGrey, colorGrey)
                btnAddToWishlist.isEnabled = true
                btnAddToWishlistIcon.isEnabled = true
            }
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail
        const val PAYLOAD_CLICK_WISHLIST = 90
    }

}
