package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshoplist.FavoriteShopViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshoplist.OtherFavoriteShopViewHolder
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.FavoriteShopNavVisitable

class FavoriteShopListTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), FavoriteShopListTypeFactory {

    override fun type(favoriteShopModel: FavoriteShopModel): Int {
        return FavoriteShopViewHolder.LAYOUT
    }

    override fun type(otherFavoriteShopModel: OtherFavoriteShopModel): Int {
        return OtherFavoriteShopViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<FavoriteShopNavVisitable> {
        return when (viewType) {
            FavoriteShopViewHolder.LAYOUT -> FavoriteShopViewHolder(view, mainNavListener)
            OtherFavoriteShopViewHolder.LAYOUT -> OtherFavoriteShopViewHolder(view, mainNavListener)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<FavoriteShopNavVisitable>
    }
}