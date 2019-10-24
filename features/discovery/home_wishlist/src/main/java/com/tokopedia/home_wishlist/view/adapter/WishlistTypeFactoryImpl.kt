package com.tokopedia.home_wishlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel

/**
 * A Class of Implementation Type Factory Pattern.
 *
 * This class extends from [BaseAdapterTypeFactory] and implement from [HomeRecommendationTypeFactory]
 */
class WishlistTypeFactoryImpl : BaseAdapterTypeFactory(), WishlistTypeFactory {
    override fun type(wishlistItemDataModel: WishlistItemDataModel): Int {
        return 1
    }

    override fun type(recommendationCarouselDataModel: RecommendationCarouselDataModel): Int {
        return 2
    }

    /**
     * This override function from [BaseAdapterTypeFactory]
     * It return viewHolder
     * @param view the parent of the view
     * @param type the type of view
     */
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            else -> super.createViewHolder(view, type)
        }
    }
}