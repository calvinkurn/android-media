package com.tokopedia.exploreCategory.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECShimmerVHViewModel

class ECServiceAdapter(ecServiceAdapterFactory: ECServiceAdapterFactory)
    : BaseAdapter<ECServiceAdapterFactory>(ecServiceAdapterFactory) {

    fun startShimmer(){
        this.visitables.clear()
        addElement(ECShimmerVHViewModel())
    }

    fun stopShimmer(){
        this.visitables.clear()
    }
}