package com.tokopedia.browse.homepage.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceViewHolder
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel

/**
 * @author by furqan on 07/09/18.
 */

class DigitalBrowseServiceAdapter(adapterTypeFactory: DigitalBrowseServiceAdapterTypeFactory, visitables: List<Visitable<*>>) :
        BaseAdapter<DigitalBrowseServiceAdapterTypeFactory>(adapterTypeFactory, visitables) {

    fun isLoadingObject(position: Int): Boolean {
        return visitables[position] is LoadingModel
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        if (position == itemCount - 1 && visitables[position] !is LoadingModel) {
            (holder as DigitalBrowseServiceViewHolder).bindLastItem(
                    visitables[position] as DigitalBrowseServiceCategoryViewModel)
        } else {
            super.onBindViewHolder(holder, position)
        }
    }
}
