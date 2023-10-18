package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationCarouselItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2RecommendationCarouselViewHolder(
    private val binding: WishlistV2RecommendationCarouselItemBinding,
    private val actionListener: WishlistV2Adapter.ActionListener?
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(element: WishlistV2TypeLayoutData, adapterPosition: Int, isShowCheckbox: Boolean) {
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
            if (element.dataObject is WishlistV2RecommendationDataModel) {
                val data = element.dataObject.recommendationProductCardModelData
                binding.carousel.bindCarouselProductCardViewGrid(
                    productCardModelList = data,
                    carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                        override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val recommendationItem = element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) ?: return

                            if (recommendationItem.isTopAds) {
                                ImpresionTask(this::class.java.simpleName).execute(recommendationItem.trackerImageUrl)
                            }
                            actionListener?.onRecommendationCarouselItemImpression(recommendationItem, adapterPosition)
                        }

                        override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder {
                            return element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) as ImpressHolder
                        }
                    },
                    carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val recommendationItem = element.dataObject.listRecommendationItem.getOrNull(carouselProductCardPosition) ?: return
                            actionListener?.onRecommendationCarouselItemClick(recommendationItem, carouselProductCardPosition)
                        }
                    }
                )
            }
        }
    }
}
