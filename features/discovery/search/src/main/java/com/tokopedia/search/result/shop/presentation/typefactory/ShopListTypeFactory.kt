package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.shop.presentation.model.*
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopRecommendationTitleViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopSuggestionViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel

internal interface ShopListTypeFactory {

    fun type(shopCpmViewModel: ShopCpmViewModel): Int

    fun type(shopItem: ShopViewModel.ShopItem): Int

    fun type(shopEmptySearchViewModel: ShopEmptySearchViewModel): Int

    fun type(shopRecommendationTitleViewModel: ShopRecommendationTitleViewModel): Int

    fun type(shopSuggestionViewModel: ShopSuggestionViewModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>

}
