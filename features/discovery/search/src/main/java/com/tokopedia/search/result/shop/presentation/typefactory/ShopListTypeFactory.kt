package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import com.tokopedia.search.result.shop.presentation.model.ShopRecommendationTitleDataView
import com.tokopedia.search.result.shop.presentation.model.ShopSuggestionDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView

internal interface ShopListTypeFactory {

    fun type(shopCpmDataView: ShopCpmDataView): Int

    fun type(shopDataItem: ShopDataView.ShopItem): Int

    fun type(shopEmptySearchDataView: ShopEmptySearchDataView): Int

    fun type(shopRecommendationTitleDataView: ShopRecommendationTitleDataView): Int

    fun type(shopSuggestionDataView: ShopSuggestionDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>

}
