package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class BrandlistPageAdapterTypeFactory(
        private val trackingListener: BrandlistPageTrackingListener,
        private val searchListener: AllBrandNotFoundViewHolder.Listener
) : BaseAdapterTypeFactory(), BrandlistPageTypeFactory {

    override fun type(featuredBrandViewModel: FeaturedBrandViewModel): Int {
        return if (featuredBrandViewModel.featuredBrands.isEmpty()) HideViewHolder.LAYOUT
        else FeaturedBrandViewHolder.LAYOUT
    }

    override fun type(popularBrandViewModel: PopularBrandViewModel): Int {
        return if (popularBrandViewModel.popularBrands.isEmpty()) HideViewHolder.LAYOUT
        else PopularBrandViewHolder.LAYOUT
    }

    override fun type(newBrandViewModel: NewBrandViewModel): Int {
        return if (newBrandViewModel.newBrands.isEmpty()) HideViewHolder.LAYOUT
        else NewBrandViewHolder.LAYOUT
    }

    override fun type(allBrandHeaderViewModel: AllBrandHeaderViewModel): Int {
        return if (allBrandHeaderViewModel.title.isNullOrEmpty()) HideViewHolder.LAYOUT
        else AllBrandHeaderViewHolder.LAYOUT
    }

    override fun type(allBrandGroupHeaderViewModel: AllBrandGroupHeaderViewModel): Int {
        return AllBrandGroupHeaderViewHolder.LAYOUT
    }

    override fun type(allBrandViewModel: AllBrandViewModel): Int {
        return AllBrandViewHolder.LAYOUT
    }

    override fun type(allbrandNotFoundViewModel: AllbrandNotFoundViewModel): Int {
        return AllBrandNotFoundViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FeaturedBrandViewHolder.LAYOUT -> FeaturedBrandViewHolder(parent, trackingListener)
            PopularBrandViewHolder.LAYOUT -> PopularBrandViewHolder(parent, trackingListener)
            NewBrandViewHolder.LAYOUT -> NewBrandViewHolder(parent, trackingListener)
            AllBrandHeaderViewHolder.LAYOUT -> AllBrandHeaderViewHolder(parent)
            AllBrandGroupHeaderViewHolder.LAYOUT -> AllBrandGroupHeaderViewHolder(parent)
            AllBrandViewHolder.LAYOUT -> AllBrandViewHolder(parent)
            AllBrandNotFoundViewHolder.LAYOUT -> AllBrandNotFoundViewHolder(parent, searchListener)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}