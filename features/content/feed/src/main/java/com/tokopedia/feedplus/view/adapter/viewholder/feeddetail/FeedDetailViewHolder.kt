package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailProductModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.list_feed_detail.view.*

/**
 * @author by nisie on 5/18/17.
 */

private const val RATING_FORMAT = 20.0


class FeedDetailViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<FeedDetailProductModel>(itemView) {

    override fun bind(feedDetailProductModel: FeedDetailProductModel) {
        itemView.run {

            productImage.setImageUrl(feedDetailProductModel.imgUrl)
            productName.text = MethodChecker.fromHtml(feedDetailProductModel.text)
            productPrice.text = feedDetailProductModel.priceFmt



            discount_layout.showWithCondition(feedDetailProductModel.isDiscount)
            discountLabel.showWithCondition(feedDetailProductModel.isDiscount)
            if (feedDetailProductModel.isDiscount) {
                discountLabel.text = feedDetailProductModel.discountFmt
                productTag.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = feedDetailProductModel.originalPriceFmt
                }
                productPrice.text = feedDetailProductModel.priceDiscountFmt

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

            setOnClickListener {
                viewListener.onGoToProductDetail(
                        feedDetailProductModel, adapterPosition)
            }
            menu.setOnClickListener {
                viewListener.onBottomSheetMenuClicked(feedDetailProductModel, itemView.context)
            }
        }
    }


    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail
    }

}