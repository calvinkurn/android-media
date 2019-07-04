package com.tokopedia.tkpd.home.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.tkpd.home.adapter.factory.WishlistAdapterFactory
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistEmptyViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistAdapter(adapterTypeFactory: WishlistAdapterFactory) : BaseAdapter<WishlistAdapterFactory>(adapterTypeFactory) {

    fun setEmptyState() {
        this.visitables.clear()
        this.visitables.add(WishlistEmptyViewModel())
    }

    fun setSearchNotFound(query: String){

    }

}
