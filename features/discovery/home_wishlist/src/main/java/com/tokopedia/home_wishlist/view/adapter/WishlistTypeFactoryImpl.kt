package com.tokopedia.home_wishlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.view.viewholder.*

/**
 * A Class of Implementation Type Factory Pattern.
 *
 * This class extends from [BaseAdapterTypeFactory] and implement from [HomeRecommendationTypeFactory]
 */
class WishlistTypeFactoryImpl : BaseAdapterTypeFactory(), WishlistTypeFactory {
    override fun type(wishlistItemDataModel: WishlistItemDataModel): Int {
        return WishlistItemDataModel.LAYOUT
    }

    override fun type(recommendationCarouselDataModel: RecommendationCarouselDataModel): Int {
        return RecommendationCarouselViewHolder.LAYOUT
    }

    override fun type(recommendationTitleDataModel: RecommendationTitleDataModel): Int {
        return RecommendationTitleViewHolder.LAYOUT
    }

    override fun type(recommendationCarouselItemDataModel: RecommendationCarouselItemDataModel): Int {
        return RecommendationCarouselItemViewHolder.LAYOUT
    }

    override fun type(recommendationItemDataModel: RecommendationItemDataModel): Int {
        return RecommendationItemViewHolder.LAYOUT
    }

    override fun type(emptyWishlistDataModel: EmptyWishlistDataModel): Int {
        return EmptyWishlistViewHolder.LAYOUT
    }

    override fun type(loadingDataModel: LoadingDataModel): Int {
        return LoadingViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadMoreDataModel): Int {
        return LoadMoreViewHolder.LAYOUT
    }

    /**
     * This override function from [BaseAdapterTypeFactory]
     * It return viewHolder
     * @param view the parent of the view
     * @param type the type of view
     */
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            WishlistItemDataModel.LAYOUT -> WishlistItemViewHolder(view)
            LoadMoreViewHolder.LAYOUT -> LoadMoreViewHolder(view)
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(view)
            EmptyWishlistViewHolder.LAYOUT -> EmptyWishlistViewHolder(view)
            RecommendationItemViewHolder.LAYOUT -> RecommendationItemViewHolder(view)
            RecommendationCarouselItemViewHolder.LAYOUT -> RecommendationCarouselItemViewHolder(view)
            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)
            RecommendationCarouselViewHolder.LAYOUT -> RecommendationCarouselViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}