package com.tokopedia.home_wishlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.model.datamodel.*

/**
 * Created by Lukas on 26/08/19
 *
 * A Interface of Type Factory Pattern.
 *
 * This interface initialize all viewType it will shown at Adapter HomeRecommendation
 */
interface WishlistTypeFactory {
    fun type(wishlistItemDataModel: WishlistItemDataModel): Int
    fun type(errorWishlistDataModel: ErrorWishlistDataModel): Int
    fun type(recommendationTitleDataModel: RecommendationTitleDataModel): Int
    fun type(recommendationCarouselDataModel: RecommendationCarouselDataModel): Int
    fun type(recommendationCarouselItemDataModel: RecommendationCarouselItemDataModel): Int
    fun type(recommendationItemDataModel: RecommendationItemDataModel): Int
    fun type(emptyWishlistDataModel: EmptyWishlistDataModel): Int
    fun type(loadingDataModel: LoadingDataModel): Int
    fun type(loadingMoreModel: LoadMoreDataModel): Int
    fun createViewHolder(view: View, type: Int): SmartAbstractViewHolder<*>
}