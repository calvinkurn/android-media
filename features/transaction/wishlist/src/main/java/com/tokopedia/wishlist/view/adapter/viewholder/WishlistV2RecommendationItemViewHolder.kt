package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2RecommendationItemViewHolder(private val binding: WishlistV2RecommendationItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                setOnClickListener {
                    item.recommId?.let { it1 -> actionListener?.onProductRecommItemClicked(it1) }
                }
            }
        }
    }
}