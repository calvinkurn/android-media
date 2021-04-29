package com.tokopedia.favorite.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopViewModel

/**
 * @author kulomady on 1/24/17.
 */
interface FavoriteTypeFactory : AdapterTypeFactory {

    fun type(viewModel: TopAdsShopViewModel?): Int

    fun type(uiModel: FavoriteShopUiModel?): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}
