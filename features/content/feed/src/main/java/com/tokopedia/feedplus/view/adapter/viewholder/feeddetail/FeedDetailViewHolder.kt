package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import android.graphics.Paint
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailProductModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

/**
 * @author by nisie on 5/18/17.
 */

private const val RATING_FORMAT = 20.0


class FeedDetailViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<FeedDetailProductModel>(itemView) {

    private var discountLayout: LinearLayout
    private var productImage: ImageUnify
    private var discountLabel: Label
    private var productPrice: Typography
    private var productName: Typography
    private var productTag: Typography
    private var rating: Typography
    private var soldInfo: Typography
    private var freeShipping: ImageView
    private var divider: View
    private var star: IconUnify
    private var menuBtn: IconUnify
    private var btnAddToCart: UnifyButton
    private var btnAddToWishlist: FrameLayout
    private var progressBar: ProgressBarUnify
    private var stockProgressBarLayout: View
    private var stockText: Typography

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

            discountLayout.showWithCondition(feedDetailProductModel.isDiscount)
            discountLabel.showWithCondition(feedDetailProductModel.isDiscount)
            if (feedDetailProductModel.isUpcoming) {
                productPrice.text = feedDetailProductModel.product.priceMaskedFmt
            } else {
                if (feedDetailProductModel.isDiscount) {
                    discountLabel.text = feedDetailProductModel.discountFmt
                    productTag.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        text = feedDetailProductModel.originalPriceFmt
                    }
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
            val soldInfoText = getString(com.tokopedia.feedcomponent.R.string.feed_common_terjual) + " " + feedDetailProductModel.totalSold.productThousandFormatted(formatLimit = 1000, isASGCDetailPage = true)

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
            if (isUpcomingAndRilisanSpecial){
                btnAddToCart.apply {
                    isEnabled = false
                    text =
                        getString(com.tokopedia.feedcomponent.R.string.btn_add_to_cart_text_disabled)
                }
            }
            btnAddToCart.setOnClickListener {
                viewListener.onAddToCartButtonClicked(feedDetailProductModel)
            }
            btnAddToWishlist.setOnClickListener {
                viewListener.onAddToWishlistButtonClicked(feedDetailProductModel)
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

    private fun setGradientColorForProgressBar(item: FeedDetailProductModel, itemView: View){
        val progressBarColor: IntArray = intArrayOf(
            ContextCompat.getColor(itemView.context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_0_color),
            ContextCompat.getColor(itemView.context, com.tokopedia.feedcomponent.R.color.feed_dms_asgc_progress_100_color)
        )
        itemView.run {
            progressBar.progressBarColor = progressBarColor
            val value = (item.product.stockSoldPercentage).roundToInt()
            progressBar.setValue(value, true)
            stockText.text = item.product.stockWording
            stockProgressBarLayout.visible()
        }
    }


    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail
    }

}