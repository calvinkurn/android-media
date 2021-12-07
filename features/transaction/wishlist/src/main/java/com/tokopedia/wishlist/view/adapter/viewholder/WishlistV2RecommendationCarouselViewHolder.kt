package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationCarouselItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2RecommendationCarouselViewHolder(private val binding: WishlistV2RecommendationCarouselItemBinding,
                                                 private val actionListener: WishlistV2Adapter.ActionListener?) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(element: WishlistV2TypeLayoutData) {
            if (element.dataObject is WishlistV2RecommendationDataModel) {
                val data = element.dataObject.recommendationData
                val listRecommId = element.dataObject.listRecommendationId
                binding.carousel.bindCarouselProductCardViewGrid(
                    productCardModelList = data,
                    carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener{
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            if (listRecommId.isNotEmpty()) {
                                actionListener?.onProductRecommItemClicked(listRecommId[carouselProductCardPosition].toString())
                            }
                        }
                    },
                )
            }
        }
}