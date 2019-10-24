package com.tokopedia.home_wishlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel

/**
 * Created by Lukas on 26/08/19
 *
 * A Interface of Type Factory Pattern.
 *
 * This interface initialize all viewType it will shown at Adapter HomeRecommendation
 */
interface WishlistTypeFactory {
    fun type(wishlistItemDataModel: WishlistItemDataModel): Int
    fun type(recommendationCarouselDataModel: RecommendationCarouselDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}