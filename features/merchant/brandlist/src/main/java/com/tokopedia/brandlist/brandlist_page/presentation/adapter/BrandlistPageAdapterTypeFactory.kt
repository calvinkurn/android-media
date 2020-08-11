package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class BrandlistPageAdapterTypeFactory(
        private val trackingListener: BrandlistPageTrackingListener,
        private val searchListener: AllBrandNotFoundViewHolder.Listener
) : BaseAdapterTypeFactory(), BrandlistPageTypeFactory {

    override fun type(featuredBrandUiModel: FeaturedBrandUiModel): Int {
        return if (featuredBrandUiModel.featuredBrands.isEmpty()) HideViewHolder.LAYOUT
        else FeaturedBrandViewHolder.LAYOUT
    }

    override fun type(popularBrandUiModel: PopularBrandUiModel): Int {
        return if (popularBrandUiModel.popularBrands.isEmpty()) HideViewHolder.LAYOUT
        else PopularBrandViewHolder.LAYOUT
    }

    override fun type(newBrandUiModel: NewBrandUiModel): Int {
        return if (newBrandUiModel.newBrands.isEmpty()) HideViewHolder.LAYOUT
        else NewBrandViewHolder.LAYOUT
    }

    override fun type(allBrandHeaderUiModel: AllBrandHeaderUiModel): Int {
        return if (allBrandHeaderUiModel.title.isNullOrEmpty()) HideViewHolder.LAYOUT
        else AllBrandHeaderViewHolder.LAYOUT
    }

    override fun type(allBrandGroupHeaderUiModel: AllBrandGroupHeaderUiModel): Int {
        return AllBrandGroupHeaderViewHolder.LAYOUT
    }

    override fun type(allBrandUiModel: AllBrandUiModel): Int {
        return AllBrandViewHolder.LAYOUT
    }

    override fun type(allbrandNotFoundUiModel: AllbrandNotFoundUiModel): Int {
        return AllBrandNotFoundViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return AllBrandLoadingRecomViewHolder.LAYOUT
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
            AllBrandLoadingRecomViewHolder.LAYOUT -> AllBrandLoadingRecomViewHolder(parent)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}