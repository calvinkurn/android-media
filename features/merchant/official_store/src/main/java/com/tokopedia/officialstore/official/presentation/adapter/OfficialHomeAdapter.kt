package com.tokopedia.officialstore.official.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class OfficialHomeAdapter(adapterTypeFactory: OfficialHomeAdapterTypeFactory):
        BaseAdapter<OfficialHomeAdapterTypeFactory>(adapterTypeFactory) {

    fun getDataByPosition(position: Int): Visitable<*> {
        return this.visitables[position]
    }
}