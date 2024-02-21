package com.tokopedia.wishlist.collection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.asTrackingModel
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding

class WishlistCollectionRecommendationItemViewHolder(
    private val binding: WishlistRecommendationItemBinding,
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

                addOnImpression1pxListener(item.recommItem) {
                    AppLogRecommendation.sendProductShowAppLog(item.recommItem.asTrackingModel())
                }
            }
        }
    }
}
