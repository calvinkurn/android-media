package com.tokopedia.sellerorder.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

class SomFilterAdapter(adapterTypeFactory: SomFilterAdapterTypeFactory): BaseAdapter<SomFilterAdapterTypeFactory>(adapterTypeFactory) {

    fun updateData(dataList: List<SomFilterUiModel>) {
        visitables.clear()
        visitables.addAll(dataList)
        notifyDataSetChanged()
    }
}