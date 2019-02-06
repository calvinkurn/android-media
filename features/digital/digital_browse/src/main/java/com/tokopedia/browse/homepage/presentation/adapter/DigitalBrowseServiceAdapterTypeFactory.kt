package com.tokopedia.browse.homepage.presentation.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceShimmeringViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceViewHolder
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseServiceAdapterTypeFactory(private val categoryListener: DigitalBrowseServiceViewHolder.CategoryListener) :
        BaseAdapterTypeFactory(), AdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == DigitalBrowseServiceShimmeringViewHolder.LAYOUT) {
            DigitalBrowseServiceShimmeringViewHolder(parent)
        } else if (type == DigitalBrowseServiceViewHolder.LAYOUT) {
            DigitalBrowseServiceViewHolder(parent, categoryListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: LoadingModel): Int {
        return DigitalBrowseServiceShimmeringViewHolder.LAYOUT
    }

    fun type(viewModel: DigitalBrowseServiceCategoryViewModel): Int {
        return DigitalBrowseServiceViewHolder.LAYOUT
    }
}
