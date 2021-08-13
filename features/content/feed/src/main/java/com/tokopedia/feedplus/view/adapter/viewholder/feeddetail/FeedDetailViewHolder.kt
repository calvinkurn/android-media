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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.list_feed_detail.view.*

/**
 * @author by nisie on 5/18/17.
 */

private const val TYPE_DISCOUNT = "discount"
private const val TYPE_CASHBACK = "cashback"

class FeedDetailViewHolder(itemView: View, private val viewListener: FeedPlusDetailListener) : AbstractViewHolder<FeedDetailItemModel>(itemView) {

    override fun bind(feedDetailViewModel: FeedDetailItemModel) {
        itemView.run {
            ImageHandler.LoadImage(productImage, feedDetailViewModel.imageSource)

            productName.text = MethodChecker.fromHtml(feedDetailViewModel.name)
            productPrice.text = feedDetailViewModel.price

            feedDetailViewModel.tags.firstOrNull()?.let {
                promoTagParent.show()
                when (it.type) {
                    TYPE_CASHBACK -> {
                        setCashBackTypeTag(tagTypeText, it.text)
                        textSlashedPrice.hide()
                    }
                    TYPE_DISCOUNT -> {
                        setDiscountTypeTag(tagTypeText, it.text)
                        setSlashedPriceText(textSlashedPrice, feedDetailViewModel.priceOriginal)
                    }
                    else -> {
                        tagTypeText.hide()
                        textSlashedPrice.hide()
                    }
                }
            } ?: promoTagParent.hide()

            if (feedDetailViewModel.rating > 0) {
                feedbackParent.show()
                ratingText.text = feedDetailViewModel.rating.toFloat().toString()
                val ratingCountText = "(${feedDetailViewModel.countReview})"
                ratingCount.text = ratingCountText
            } else {
                feedbackParent.hide()
            }

            setOnClickListener {
                viewListener.onGoToProductDetail(
                        feedDetailViewModel, adapterPosition)
            }
        }
    }

    private fun setSlashedPriceText(textSlashedPrice: Typography?, priceOriginal: String) {
        textSlashedPrice?.run {
            text = priceOriginal
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }
    }

    private fun setDiscountTypeTag(tagTypeText: Typography?, typeText: String) {
        tagTypeText?.run {
            text = typeText
            background = MethodChecker.getDrawable(itemView.context, com.tokopedia.feedcomponent.R.drawable.discount_text_background)
            setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
            show()
        }

    }

    private fun setCashBackTypeTag(tagTypeText: Typography?, typeText: String) {
        tagTypeText?.run {
            text = typeText
            background = MethodChecker.getDrawable(itemView.context, com.tokopedia.feedcomponent.R.drawable.cashback_text_background)
            setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            show()
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail
    }

}