package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2CountManageRowData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2StickyItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2CountManageRowItemViewHolder(
    private val binding: WishlistV2StickyItemBinding,
    private val actionListener: WishlistV2Adapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, isShowCheckbox: Boolean) {
        if (item.dataObject is WishlistV2CountManageRowData) {
            binding.wishlistCountLabel.text = "${item.dataObject.count}"
            binding.wishlistManageLabel.setOnClickListener {
                if (!isShowCheckbox) {
                    binding.wishlistManageLabel.text = itemView.context.getString(R.string.wishlist_cancel_manage_label)
                    actionListener?.onManageClicked(
                        showCheckbox = true,
                        isDeleteOnly = false,
                        isBulkAdd = false
                    )
                } else {
                    binding.wishlistManageLabel.text = itemView.context.getString(R.string.wishlist_manage_label)
                    actionListener?.onManageClicked(
                        showCheckbox = false,
                        isDeleteOnly = false,
                        isBulkAdd = false
                    )
                }
                item.dataObject.isBulkDeleteShow = !item.dataObject.isBulkDeleteShow
            }
        }
    }

    fun setManageLabel(label: String) {
        binding.wishlistManageLabel.text = label
    }
}
