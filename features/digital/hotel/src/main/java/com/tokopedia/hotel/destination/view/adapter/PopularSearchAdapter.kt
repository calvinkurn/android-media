package com.tokopedia.hotel.destination.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

/**
 * @author by jessica on 31/03/19
 */

class PopularSearchAdapter(adapterTypeFactory: PopularSearchTypeFactory, visitables: List<Visitable<*>>) :
        BaseAdapter<PopularSearchTypeFactory>(adapterTypeFactory, visitables)
