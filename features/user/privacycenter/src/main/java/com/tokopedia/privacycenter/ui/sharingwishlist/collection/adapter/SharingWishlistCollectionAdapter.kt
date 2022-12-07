package com.tokopedia.privacycenter.ui.sharingwishlist.collection.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.data.WishlistCollectionsDataModel

class SharingWishlistCollectionAdapter constructor(
    listener: Listener
) : BaseAdapter<WishlistCollectionsDataModel>() {

    interface Listener {
        fun onCollectionItemClicked(data: WishlistCollectionsDataModel)
    }

    init {
        delegatesManager.addDelegate(SharingWishlistCollectionAdapterDelegate(listener))
    }
}
