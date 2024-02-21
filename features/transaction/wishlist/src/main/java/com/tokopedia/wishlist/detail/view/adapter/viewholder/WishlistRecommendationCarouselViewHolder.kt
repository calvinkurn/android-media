package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.asTrackingModel
import com.tokopedia.wishlist.databinding.WishlistRecommendationCarouselItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistRecommendationDataModel
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistRecommendationCarouselViewHolder(
    private val binding: WishlistRecommendationCarouselItemBinding,
    private val actionListener: WishlistAdapter.ActionListener?
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(element: WishlistTypeLayoutData, adapterPosition: Int, isShowCheckbox: Boolean) {
        if (isShowCheckbox) {
            binding.root.gone()
            val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                height = 0
                width = 0
            }
            binding.root.layoutParams = params
        } else {
            binding.root.visible()
            val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                isFullSpan = true
            }
            binding.root.layoutParams = params
            if (element.dataObject is WishlistRecommendationDataModel) {
                val data = element.dataObject.recommendationProductCardModelData
                binding.carousel.bindCarouselProductCardViewGrid(
                    productCardModelList = data,
                    carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                        override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val recommendationItem = element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) ?: return
                            actionListener?.onRecommendationCarouselItemImpression(recommendationItem, adapterPosition)
                            AppLogRecommendation.sendProductShowAppLog(recommendationItem.asTrackingModel())
                        }

                        override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder {
                            return element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) as ImpressHolder
                        }
                    },
                    carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val recommendationItem = element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) ?: return
                            actionListener?.onRecommendationCarouselItemClick(recommendationItem, carouselProductCardPosition)
                            AppLogRecommendation.sendProductClickAppLog(recommendationItem.asTrackingModel())
                        }
                    }
                )
            }
        }
    }
}
