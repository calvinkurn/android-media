package com.tokopedia.home_wishlist.view.adapter

import android.view.View
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartTypeFactory

/**
 * Created by Lukas on 26/08/19
 *
 * A Interface of Type Factory Pattern.
 *
 * This interface initialize all viewType it will shown at Adapter HomeRecommendation
 */
interface WishlistTypeFactory : SmartTypeFactory{
    fun type(wishlistItemDataModel: WishlistItemDataModel): Int
    fun type(errorWishlistDataModel: ErrorWishlistDataModel): Int
    fun type(recommendationTitleDataModel: RecommendationTitleDataModel): Int
    fun type(recommendationCarouselDataModel: RecommendationCarouselDataModel): Int
    fun type(recommendationCarouselItemDataModel: RecommendationCarouselItemDataModel): Int
    fun type(recommendationItemDataModel: RecommendationItemDataModel): Int
    fun type(emptyWishlistDataModel: EmptyWishlistDataModel): Int
    fun type(emptyWishlistDataModel: EmptySearchWishlistDataModel): Int
    fun type(loadingDataModel: LoadingDataModel): Int
    fun type(loadingMoreModel: LoadMoreDataModel): Int
    override fun createViewHolder(view: View, type: Int): SmartAbstractViewHolder<*>
}