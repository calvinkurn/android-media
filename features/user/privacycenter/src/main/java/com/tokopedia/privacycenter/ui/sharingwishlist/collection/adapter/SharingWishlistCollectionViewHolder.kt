package com.tokopedia.privacycenter.ui.sharingwishlist.collection.adapter

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.privacycenter.data.WishlistCollectionsDataModel
import com.tokopedia.privacycenter.databinding.SharingWishlistItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class SharingWishlistCollectionViewHolder(
    itemView: View,
    private val listener: SharingWishlistCollectionAdapter.Listener
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding<SharingWishlistItemBinding>()

    fun onBind(data: WishlistCollectionsDataModel) {
        itemViewBinding?.apply {
            wishlistName.text = data.name
            wishlistCounter.text = "${data.totalItem} ${data.itemText}"
            wishlistSettingButton.setOnClickListener {
                listener.onCollectionItemClicked(data)
            }
        }
    }
}
