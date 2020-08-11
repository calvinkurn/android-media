package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.brandlist_search.presentation.fragment.BrandlistSearchFragment


class BrandlistSearchAdapterTypeFactory(
        private val brandlistSearchFragment: BrandlistSearchFragment,
        private val searchListener: BrandlistSearchRecommendationNotFoundViewHolder.Listener
) : BaseAdapterTypeFactory(), BrandlistSearchTypeFactory {

    override fun type(brandlistSearchResultUiModel: BrandlistSearchResultUiModel): Int {
        return BrandlistSearchResultViewHolder.LAYOUT
    }

    override fun type(brandlistSearchRecommendationUiModel: BrandlistSearchRecommendationUiModel): Int {
        return BrandlistSearchRecommendationViewHolder.LAYOUT
    }

    override fun type(brandlistSearchNotFoundUiModel: BrandlistSearchNotFoundUiModel): Int {
        return BrandlistSearchNotFoundViewHolder.LAYOUT
    }

    override fun type(brandlistSearchHeaderUiModel: BrandlistSearchHeaderUiModel): Int {
        return BrandlistSearchHeaderViewHolder.LAYOUT
    }

    override fun type(brandlistSearchShimmeringUiModel: BrandlistSearchShimmeringUiModel): Int {
        return BrandlistSearchShimmeringViewHolder.LAYOUT
    }

    override fun type(brandlistSearchAllBrandGroupHeaderUiModel: BrandlistSearchAllBrandGroupHeaderUiModel): Int {
        return BrandlistSearchGroupHeaderViewHolder.LAYOUT
    }

    override fun type(brandlistSearchRecommendationNotFoundUiModel: BrandlistSearchRecommendationNotFoundUiModel): Int {
        return BrandlistSearchRecommendationNotFoundViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BrandlistSearchResultViewHolder.LAYOUT -> BrandlistSearchResultViewHolder(parent)
            BrandlistSearchRecommendationViewHolder.LAYOUT -> BrandlistSearchRecommendationViewHolder(parent)
            BrandlistSearchNotFoundViewHolder.LAYOUT -> BrandlistSearchNotFoundViewHolder(parent, brandlistSearchFragment)
            BrandlistSearchHeaderViewHolder.LAYOUT -> BrandlistSearchHeaderViewHolder(parent)
            BrandlistSearchShimmeringViewHolder.LAYOUT -> BrandlistSearchShimmeringViewHolder(parent)
            BrandlistSearchGroupHeaderViewHolder.LAYOUT -> BrandlistSearchGroupHeaderViewHolder(parent)
            BrandlistSearchRecommendationNotFoundViewHolder.LAYOUT -> BrandlistSearchRecommendationNotFoundViewHolder(parent, searchListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}