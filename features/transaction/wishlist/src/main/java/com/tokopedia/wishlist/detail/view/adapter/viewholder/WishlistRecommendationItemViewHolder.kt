package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationType
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.asProductTrackModel
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter
import com.tokopedia.wishlist.collection.util.WishlistCollectionUtils.clickWithDebounce
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding

class WishlistRecommendationItemViewHolder(
    private val binding: WishlistRecommendationItemBinding,
    private val actionListener: WishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    fun bind(item: WishlistTypeLayoutData, adapterPosition: Int) {
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                clickWithDebounce {
                    AppLogRecommendation.sendProductClickAppLog(
                        item.recommItem.asProductTrackModel(type = AppLogRecommendationType.VERTICAL)
                    )
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
                    AppLogRecommendation.sendProductShowAppLog(
                        item.recommItem.asProductTrackModel(type = AppLogRecommendationType.VERTICAL)
                    )
                }
            }
        }
    }
}
