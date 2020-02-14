package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*


class BrandlistSearchAdapterTypeFactory : BaseAdapterTypeFactory(), BrandlistSearchTypeFactory {

    override fun type(brandlistSearchResultViewModel: BrandlistSearchResultViewModel): Int {
        return BrandlistSearchResultViewHolder.LAYOUT
    }

    override fun type(brandlistSearchRecommendationViewModel: BrandlistSearchRecommendationViewModel): Int {
        return BrandlistSearchRecommendationViewHolder.LAYOUT
    }

    override fun type(brandlistSearchNotFoundViewModel: BrandlistSearchNotFoundViewModel): Int {
        return BrandlistSearchNotFoundViewHolder.LAYOUT
    }

    override fun type(brandlistSearchRecommendationTextViewModel: BrandlistSearchRecommendationTextViewModel): Int {
        return BrandlistSearchRecommendationTextViewHolder.LAYOUT
    }

    override fun type(brandlistSearchShimmeringViewModel: BrandlistSearchShimmeringViewModel): Int {
        return BrandlistSearchShimmeringViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BrandlistSearchResultViewHolder.LAYOUT -> BrandlistSearchResultViewHolder(parent)
            BrandlistSearchRecommendationViewHolder.LAYOUT -> BrandlistSearchRecommendationViewHolder(parent)
            BrandlistSearchNotFoundViewHolder.LAYOUT -> BrandlistSearchNotFoundViewHolder(parent)
            BrandlistSearchRecommendationTextViewHolder.LAYOUT -> BrandlistSearchRecommendationTextViewHolder(parent)
            BrandlistSearchShimmeringViewHolder.LAYOUT -> BrandlistSearchShimmeringViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}