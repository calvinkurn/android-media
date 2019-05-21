package com.tokopedia.checkout.view.feature.emptycart2.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface EmptyCartTypeFactory {

    fun type(viewModel: PromoUiModel): Int

    fun type(viewModel: EmptyCartPlaceholderUiModel): Int

    fun type(viewModel: RecentViewUiModel): Int

    fun type(viewModel: WishlistUiModel): Int

    fun type(viewModel: RecommendationUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}