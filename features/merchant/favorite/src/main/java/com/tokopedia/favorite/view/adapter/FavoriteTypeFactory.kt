package com.tokopedia.favorite.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopUiModel

/**
 * @author kulomady on 1/24/17.
 */
interface FavoriteTypeFactory : AdapterTypeFactory {

    fun type(uiModel: TopAdsShopUiModel?): Int

    fun type(uiModel: FavoriteShopUiModel?): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}
