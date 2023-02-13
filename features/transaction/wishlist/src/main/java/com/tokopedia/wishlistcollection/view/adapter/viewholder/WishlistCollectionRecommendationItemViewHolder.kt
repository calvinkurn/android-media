package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionRecommendationItemViewHolder(
    private val binding: WishlistV2RecommendationItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    fun bind(item: WishlistCollectionTypeLayoutData, adapterPosition: Int) {
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                setOnClickListener {
                    actionListener?.onRecommendationItemClick(item.recommItem, adapterPosition)
                }

                setImageProductViewHintListener(
                    item.recommItem,
                    object : ViewHintListener {
                        override fun onViewHint() {
                            actionListener?.onRecommendationItemImpression(
                                item.recommItem,
                                adapterPosition
                            )
                        }
                    }
                )
            }
        }
    }
}