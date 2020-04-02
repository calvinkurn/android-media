package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.view.View
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

class ShowcaseProductItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        val CARD_ACTIVE_STATE = CardUnify.TYPE_SHADOW_ACTIVE
        val CARD_INACTIVE_STATE = CardUnify.TYPE_SHADOW
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            itemView.product_name.text = element.productName
            itemView.product_price.text = element.productPrice.getCurrencyFormatted()
            itemView.ratingBar.rating = element.ratingStarAvg
            itemView.total_review.text = itemView.resources.getString(R.string.product_total_review, element.totalReview.toString())
            ImageHandler.LoadImage(itemView.product_image, element.productImageUrl)
            renderCardState(element)
        }
    }

    fun renderCardState(element: ShowcaseProduct) {
        itemView.parent_card_view.cardType = if(element.ishighlighted) {
            CARD_ACTIVE_STATE
        } else {
            CARD_INACTIVE_STATE
        }
    }

}