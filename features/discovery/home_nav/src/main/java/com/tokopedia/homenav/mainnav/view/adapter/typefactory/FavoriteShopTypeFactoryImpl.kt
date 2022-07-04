package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop.EmptyFavoriteShopViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop.FavoriteShopItemViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop.OtherFavoriteShopViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopNavVisitable

class FavoriteShopTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), FavoriteShopTypeFactory {

    override fun type(favoriteShopModel: FavoriteShopModel): Int {
        return FavoriteShopItemViewHolder.LAYOUT
    }

    override fun type(otherFavoriteShopModel: OtherFavoriteShopModel): Int {
        return OtherFavoriteShopViewHolder.LAYOUT
    }

    override fun type(emptyStateFavoriteShopDataModel: EmptyStateFavoriteShopDataModel): Int {
        return EmptyFavoriteShopViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<FavoriteShopNavVisitable> {
        return when (viewType) {
            FavoriteShopItemViewHolder.LAYOUT -> FavoriteShopItemViewHolder(view, mainNavListener)
            OtherFavoriteShopViewHolder.LAYOUT -> OtherFavoriteShopViewHolder(view, mainNavListener)
            EmptyFavoriteShopViewHolder.LAYOUT -> EmptyFavoriteShopViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<FavoriteShopNavVisitable>
    }
}