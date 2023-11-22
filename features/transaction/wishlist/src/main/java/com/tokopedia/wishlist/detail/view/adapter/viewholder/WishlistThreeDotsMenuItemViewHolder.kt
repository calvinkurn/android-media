package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Label
import com.tokopedia.wishlist.databinding.BottomsheetWishlistThreeDotsMenuItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistUiModel
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_ADD_ITEM_TO_COLLECTION
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_ADD_WISHLIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_CHECK_SHOP
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_CHECK_SIMILAR_PRODUCT
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_DELETE_WISHLIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_LABEL_BARU
import com.tokopedia.wishlist.detail.util.WishlistConsts.MENU_SHARE_LINK_PRODUCT
import com.tokopedia.wishlist.detail.view.bottomsheet.BottomSheetThreeDotsMenuWishlist

class WishlistThreeDotsMenuItemViewHolder(
    private val binding: BottomsheetWishlistThreeDotsMenuItemBinding,
    private val listener: BottomSheetThreeDotsMenuWishlist.BottomSheetListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(wishlistItem: WishlistUiModel.Item, additionalButtonsItem: WishlistUiModel.Item.Buttons.AdditionalButtonsItem) {
        when (additionalButtonsItem.action) {
            MENU_CHECK_SIMILAR_PRODUCT -> {
                binding.run {
                    menuIcon.setImage(IconUnify.PRODUCT)
                    menuLabel.gone()
                }
            }
            MENU_CHECK_SHOP -> {
                binding.run {
                    menuIcon.setImage(IconUnify.SHOP)
                    menuLabel.gone()
                }
            }
            MENU_SHARE_LINK_PRODUCT -> {
                binding.run {
                    menuIcon.setImage(IconUnify.SHARE_MOBILE)
                    menuLabel.gone()
                }
            }
            MENU_ADD_ITEM_TO_COLLECTION -> {
                binding.run {
                    menuIcon.setImage(IconUnify.PRODUCT_NEXT)
                    menuLabel.visible()
                    menuLabel.setLabelType(Label.HIGHLIGHT_DARK_RED)
                    menuLabel.text = MENU_LABEL_BARU
                }
            }
            MENU_DELETE_WISHLIST -> {
                binding.run {
                    menuIcon.setImage(IconUnify.DELETE)
                    menuLabel.gone()
                }
            }
            MENU_ADD_WISHLIST -> {
                binding.run {
                    menuIcon.setImage(IconUnify.HEART)
                    menuLabel.gone()
                }
            }
        }
        binding.menuItem.text = additionalButtonsItem.text
        binding.root.setOnClickListener {
            listener?.onThreeDotsMenuItemSelected(wishlistItem, additionalButtonsItem)
        }
    }
}
