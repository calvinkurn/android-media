package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlistcollection.util.WishlistCollectionUtils.clickWithDebounce

class WishlistV2RecommendationItemViewHolder(
    private val binding: WishlistV2RecommendationItemBinding,
    private val actionListener: WishlistV2Adapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    fun bind(item: WishlistV2TypeLayoutData, adapterPosition: Int) {
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                clickWithDebounce {
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
