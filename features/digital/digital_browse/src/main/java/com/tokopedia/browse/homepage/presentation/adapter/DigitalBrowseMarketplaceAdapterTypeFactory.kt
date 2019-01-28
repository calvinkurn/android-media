package com.tokopedia.browse.homepage.presentation.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseCategoryViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseMarketplaceShimmeringViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowsePopularViewHolder
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseMarketplaceAdapterTypeFactory(private val popularBrandListener: DigitalBrowsePopularViewHolder.PopularBrandListener,
                                                 private val categoryListener: DigitalBrowseCategoryViewHolder.CategoryListener) :
        BaseAdapterTypeFactory(), AdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT) {
            DigitalBrowseMarketplaceShimmeringViewHolder(parent)
        } else if (type == DigitalBrowseCategoryViewHolder.LAYOUT) {
            DigitalBrowseCategoryViewHolder(parent, categoryListener)
        } else if (type == DigitalBrowsePopularViewHolder.LAYOUT) {
            DigitalBrowsePopularViewHolder(parent, popularBrandListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: LoadingModel): Int {
        return DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT
    }

    fun type(viewModel: DigitalBrowseRowViewModel): Int {
        return DigitalBrowseCategoryViewHolder.LAYOUT
    }

    fun type(viewModel: DigitalBrowsePopularBrandsViewModel): Int {
        return DigitalBrowsePopularViewHolder.LAYOUT
    }
}
