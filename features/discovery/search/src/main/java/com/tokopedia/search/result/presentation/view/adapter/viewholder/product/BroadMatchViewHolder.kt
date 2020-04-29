package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import kotlinx.android.synthetic.main.search_result_product_broad_match_layout.view.*

class BroadMatchViewHolder(
        itemView: View,
        private val broadMatchListener: BroadMatchListener
) : AbstractViewHolder<BroadMatchViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_broad_match_layout
    }

    override fun bind(element: BroadMatchViewModel) {
        bindTitle(element)
        bindSeeMore(element)
        setupRecyclerView(element)
    }

    private fun bindTitle(broadMatchViewModel: BroadMatchViewModel) {
        itemView.searchBroadMatchTitle?.text = broadMatchViewModel.keyword
    }

    private fun bindSeeMore(broadMatchViewModel: BroadMatchViewModel) {
        itemView.searchBroadMatchSeeMore?.showWithCondition(broadMatchViewModel.applink.isNotEmpty())

        itemView.searchBroadMatchSeeMore?.setOnClickListener {
            broadMatchListener.onBroadMatchSeeMoreClicked(broadMatchViewModel)
        }
    }

    private fun setupRecyclerView(dataModel: BroadMatchViewModel){
        val products = dataModel.broadMatchItemViewModelList
        itemView.searchBroadMatchList?.bindCarouselProductCardViewGrid(
                productCardModelList = products.map {
                    ProductCardModel(
                            productName = it.name,
                            formattedPrice = it.priceString,
                            productImageUrl = it.imageUrl,
                            reviewCount = it.countReview,
                            ratingCount = it.rating
                    )
                },
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return
                        product.let {
                            broadMatchListener.onBroadMatchItemClicked(product)
                        }
                    }
                }
        )
    }
}