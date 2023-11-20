package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.WishlistStickyItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistCountManageRowData
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistCountManageRowItemViewHolder(
    private val binding: WishlistStickyItemBinding,
    private val actionListener: WishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistTypeLayoutData, isShowCheckbox: Boolean) {
        if (item.dataObject is WishlistCountManageRowData) {
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
