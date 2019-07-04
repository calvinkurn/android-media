package com.tokopedia.tkpd.home.wishlist.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistAdapterFactory
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptySearchViewModel
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptyViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistAdapter(adapterTypeFactory: WishlistAdapterFactory) : BaseAdapter<WishlistAdapterFactory>(adapterTypeFactory) {

    fun setEmptyState() {
        this.visitables.clear()
        addElement(WishlistEmptyViewModel())
    }

    fun setSearchNotFound(query: String){
        this.visitables.clear()
        addElement(WishlistEmptySearchViewModel(query))
    }

}
