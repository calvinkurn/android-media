package com.tokopedia.checkout.view.feature.emptycart2.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.*
import com.tokopedia.checkout.view.feature.emptycart2.viewholder.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

open class EmptyCartAdapterTypeFactory(val listener: ActionListener) : BaseAdapterTypeFactory(), EmptyCartTypeFactory {

    override fun type(viewModel: PromoUiModel): Int {
        return PromoViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyCartPlaceholderUiModel): Int {
        return EmptyCartPlaceholderViewHolder.LAYOUT
    }

    override fun type(viewModel: RecentViewUiModel): Int {
        return RecentViewViewHolder.LAYOUT
    }

    override fun type(viewModel: WishlistUiModel): Int {
        return WishlistViewHolder.LAYOUT
    }

    override fun type(viewModel: RecommendationUiModel): Int {
        return RecommendationViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            PromoViewHolder.LAYOUT -> PromoViewHolder(view, listener)
            EmptyCartPlaceholderViewHolder.LAYOUT -> EmptyCartPlaceholderViewHolder(view, listener)
            RecentViewViewHolder.LAYOUT -> RecentViewViewHolder(view)
            WishlistViewHolder.LAYOUT -> WishlistViewHolder(view)
            RecommendationViewHolder.LAYOUT -> RecommendationViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }

}