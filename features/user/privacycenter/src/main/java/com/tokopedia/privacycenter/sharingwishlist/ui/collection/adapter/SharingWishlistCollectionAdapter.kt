package com.tokopedia.privacycenter.sharingwishlist.ui.collection.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.sharingwishlist.domain.data.WishlistCollectionsDataModel

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
