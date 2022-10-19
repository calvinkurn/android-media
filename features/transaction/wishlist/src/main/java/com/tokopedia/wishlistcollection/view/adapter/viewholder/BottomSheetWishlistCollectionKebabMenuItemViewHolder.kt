package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.wishlist.databinding.BottomsheetKebabMenuWishlistCollectionItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetKebabActionItemData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_DELETE_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_SHARE_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_UPDATE_COLLECTION
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetKebabMenuWishlistCollection

class BottomSheetWishlistCollectionKebabMenuItemViewHolder(private val binding: BottomsheetKebabMenuWishlistCollectionItemBinding,
                                                           private val listener: BottomSheetKebabMenuWishlistCollection.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        actionItem: BottomSheetKebabActionItemData,
        _collectionId: String,
        _collectionName: String
    ) {
        binding.run {
            labelKebabMenu.text = actionItem.text
        }
        when (actionItem.action) {
            ACTION_KEBAB_UPDATE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.EDIT)
                    root.setOnClickListener {
                        listener?.onEditCollection(_collectionId, _collectionName)
                    }
                }
            }
            ACTION_KEBAB_SHARE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.SHARE_MOBILE)
                }
            }
            ACTION_KEBAB_DELETE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.DELETE)
                    root.setOnClickListener {
                        listener?.onDeleteCollection(_collectionId, _collectionName)
                    }
                }
            }
        }
    }
}
