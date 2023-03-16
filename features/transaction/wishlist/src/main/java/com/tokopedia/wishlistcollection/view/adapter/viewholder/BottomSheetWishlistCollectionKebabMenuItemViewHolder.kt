package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.wishlist.databinding.BottomsheetKebabMenuWishlistCollectionItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetKebabActionItemData
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_DELETE_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_MANAGE_ITEMS_IN_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_SHARE_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.ACTION_KEBAB_UPDATE_COLLECTION
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerBottomSheetMenu

class BottomSheetWishlistCollectionKebabMenuItemViewHolder(
    private val binding: BottomsheetKebabMenuWishlistCollectionItemBinding,
    private val listener: ActionListenerBottomSheetMenu?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        actionItem: BottomSheetKebabActionItemData,
        _collectionId: String,
        _collectionName: String,
        _collectionIndicatorTitle: String
    ) {
        binding.run {
            labelKebabMenu.text = actionItem.text
        }
        when (actionItem.action) {
            ACTION_KEBAB_UPDATE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.EDIT)
                    root.setOnClickListener {
                        listener?.onEditCollection(_collectionId, _collectionName, actionItem.text)
                    }
                }
            }
            ACTION_KEBAB_SHARE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.SHARE_MOBILE)
                    listener?.onShareItemShown(iconKebabMenu)
                    root.setOnClickListener {
                        listener?.onShareCollection(_collectionId, _collectionName, actionItem.text, _collectionIndicatorTitle)
                    }
                }
            }
            ACTION_KEBAB_DELETE_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.DELETE)
                    root.setOnClickListener {
                        listener?.onDeleteCollection(_collectionId, _collectionName, actionItem.text)
                    }
                }
            }
            ACTION_KEBAB_MANAGE_ITEMS_IN_COLLECTION -> {
                binding.run {
                    iconKebabMenu.setImage(IconUnify.BACKGROUND)
                    root.setOnClickListener {
                        listener?.onManageItemsInCollection(actionItem.text)
                    }
                }
            }
        }
    }
}
