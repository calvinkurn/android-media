package com.tokopedia.hotel.destination.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel

/**
 * @author by jessica on 31/03/19
 */

class SearchDestinationAdapter(adapterTypeFactory: SearchDestinationTypeFactory, visitables: List<Visitable<*>>) :
        BaseAdapter<SearchDestinationTypeFactory>(adapterTypeFactory, visitables) {

    override fun getItemCount(): Int {
        return if (isLoading) return 4 else super.getItemCount()
    }
}
