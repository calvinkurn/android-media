package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*

class BrandlistPageAdapterTypeFactory : BaseAdapterTypeFactory(), BrandlistPageTypeFactory {

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
        return AllBrandHeaderViewHolder.LAYOUT
    }

    override fun type(allBrandViewModel: AllBrandViewModel): Int {
        return AllBrandViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FeaturedBrandViewHolder.LAYOUT -> FeaturedBrandViewHolder(parent)
            PopularBrandViewHolder.LAYOUT -> PopularBrandViewHolder(parent)
            NewBrandViewHolder.LAYOUT -> NewBrandViewHolder(parent)
            AllBrandHeaderViewHolder.LAYOUT -> AllBrandHeaderViewHolder(parent)
            AllBrandViewHolder.LAYOUT -> AllBrandViewHolder(parent)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}