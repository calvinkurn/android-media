package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.list_feed_detail.view.*

/**
 * @author by nisie on 5/18/17.
 */

private const val RATING_FORMAT = 20.0


class FeedDetailViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<ProductFeedDetailViewModelNew>(itemView) {

    override fun bind(productFeedDetailViewModelNew: ProductFeedDetailViewModelNew) {
        itemView.run {
            ImageHandler.LoadImage(productImage, productFeedDetailViewModelNew.imgUrl)

            productName.text = MethodChecker.fromHtml(productFeedDetailViewModelNew.text)
            productPrice.text = productFeedDetailViewModelNew.priceFmt

            if (productFeedDetailViewModelNew.isDiscount) {
                productTag.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = productFeedDetailViewModelNew.originalPriceFmt
                }
                productFeedDetailViewModelNew.text = productFeedDetailViewModelNew.discountFmt
                productPrice.text = productFeedDetailViewModelNew.priceDiscountFmt

            }

            freeShipping.showWithCondition(productFeedDetailViewModelNew.isFreeShipping)
            if (productFeedDetailViewModelNew.isFreeShipping) {
                freeShipping.loadImage(productFeedDetailViewModelNew.freeShippingURL)
            }

            rating.text = String.format("%.1f", productFeedDetailViewModelNew.rating.toDouble() / RATING_FORMAT)
            val soldInfoText = getString(com.tokopedia.feedcomponent.R.string.feed_common_terjual) + " " + productFeedDetailViewModelNew.totalSold.toString()
            soldInfo.text = soldInfoText
            star.showWithCondition(productFeedDetailViewModelNew.rating != 0)
            divider.showWithCondition(productFeedDetailViewModelNew.rating != 0 && productFeedDetailViewModelNew.totalSold != 0)
            rating.showWithCondition(productFeedDetailViewModelNew.rating != 0)
            soldInfo.showWithCondition(productFeedDetailViewModelNew.totalSold != 0)

            setOnClickListener {
                viewListener.onGoToProductDetail(
                        productFeedDetailViewModelNew, adapterPosition)
            }
            menu.setOnClickListener {
                viewListener.onBottomSheetMenuClicked(productFeedDetailViewModelNew, itemView.context)
            }
        }
    }


    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail
    }

}