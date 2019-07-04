package com.tokopedia.tkpd.home.wishlist.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistAdapterFactory
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptySearchViewModel
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptyViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistAdapter(adapterTypeFactory: WishlistAdapterFactory) : BaseAdapter<WishlistAdapterFactory>(adapterTypeFactory) {

    val emptyViewModel = WishlistEmptyViewModel()
    val emptySearchViewModel = WishlistEmptySearchViewModel()

    fun setEmptyState() {
        this.visitables.clear()
        addElement(emptyViewModel)
    }

    fun setSearchNotFound(query: String){
        emptySearchViewModel.query = query
        this.visitables.clear()
        addElement(emptySearchViewModel)
    }

    fun isEmptySearch(): Boolean {
        return visitables.contains(emptySearchViewModel)
    }

    fun isEmptyWishlist(): Boolean {
        return visitables.contains(emptyViewModel)
    }
}
