package com.tokopedia.privacycenter.sharingwishlist.ui.collection.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.sharingwishlist.domain.data.WishlistCollectionsDataModel

class SharingWishlistCollectionAdapterDelegate(
    private val listener: SharingWishlistCollectionAdapter.Listener
) : TypedAdapterDelegate<WishlistCollectionsDataModel, WishlistCollectionsDataModel, SharingWishlistCollectionViewHolder>(
    R.layout.sharing_wishlist_item
) {

    override fun onBindViewHolder(
        item: WishlistCollectionsDataModel,
        holder: SharingWishlistCollectionViewHolder
    ) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): SharingWishlistCollectionViewHolder {
        return SharingWishlistCollectionViewHolder(basicView, listener)
    }

}
