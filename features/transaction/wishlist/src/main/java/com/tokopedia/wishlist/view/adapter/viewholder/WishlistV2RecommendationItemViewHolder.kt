package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding

class WishlistV2RecommendationItemViewHolder(private val binding: WishlistV2RecommendationItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val cardView: ProductCardGridView by lazy {binding.wishlistProductItem}

    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is RecommendationItem) {
            cardView.run {
                setProductModel(
                    item.dataObject.toProductCardModel(hasAddToCartButton = true)
                )
            }
        }
    }
}