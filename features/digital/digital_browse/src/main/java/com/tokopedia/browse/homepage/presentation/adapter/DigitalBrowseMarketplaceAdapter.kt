package com.tokopedia.browse.homepage.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowseMarketplaceAdapter(adapterTypeFactory: DigitalBrowseMarketplaceAdapterTypeFactory, visitables: List<Visitable<*>>) :
        BaseAdapter<DigitalBrowseMarketplaceAdapterTypeFactory>(adapterTypeFactory, visitables) {

    fun isLoadingObject(position: Int): Boolean {
        return visitables[position] is LoadingModel
    }
}
