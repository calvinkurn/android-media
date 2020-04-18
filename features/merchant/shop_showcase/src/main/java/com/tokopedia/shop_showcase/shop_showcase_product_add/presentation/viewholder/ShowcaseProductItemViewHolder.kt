package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.item_add_product_showcase_grid.view.*

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductItemViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView) {

    companion object {
        val CARD_ACTIVE_STATE = CardUnify.TYPE_SHADOW_ACTIVE
        val CARD_INACTIVE_STATE = CardUnify.TYPE_SHADOW
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            itemView.product_name.text = element.productName
            itemView.product_price.text = element.productPrice.getCurrencyFormatted()
            ImageHandler.LoadImage(itemView.product_image, element.productImageUrl)
            /*itemView.product_image.setImageUrl(element.productImageUrl)*/
            renderProductRating(element)
            renderCardState(element)
        }
    }

    fun renderCardState(element: ShowcaseProduct) {
        if(element.ishighlighted) {
            itemView.parent_card_view.cardType = CARD_ACTIVE_STATE
            itemView.card_wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        } else {
            itemView.parent_card_view.cardType = CARD_INACTIVE_STATE
        }
    }

    private fun renderProductRating(element: ShowcaseProduct) {
        if(element.totalReview > 0) {
            itemView.ratingBar.rating = element.ratingStarAvg
            itemView.total_review.text = itemView.resources.getString(R.string.product_total_review, element.totalReview.toString())
            itemView.ratingBar.visibility = View.VISIBLE
            itemView.total_review.visibility = View.VISIBLE
        } else {
            itemView.ratingBar.visibility = View.GONE
            itemView.total_review.visibility = View.GONE
        }
    }

}