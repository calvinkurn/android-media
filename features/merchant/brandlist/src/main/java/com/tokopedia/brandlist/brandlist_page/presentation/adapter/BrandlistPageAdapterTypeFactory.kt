package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.FeaturedBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.NewBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.PopularBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel

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

    override fun type(allBrandViewModel: AllBrandViewModel): Int {
        return if (allBrandViewModel.allBrands.isEmpty()) HideViewHolder.LAYOUT
        else AllBrandViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FeaturedBrandViewHolder.LAYOUT -> FeaturedBrandViewHolder(parent)
            PopularBrandViewHolder.LAYOUT -> PopularBrandViewHolder(parent)
            NewBrandViewHolder.LAYOUT -> NewBrandViewHolder(parent)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}