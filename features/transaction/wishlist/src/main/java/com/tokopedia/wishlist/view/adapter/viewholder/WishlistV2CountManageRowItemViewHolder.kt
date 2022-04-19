package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2CountManageRowData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2CountManageRowItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2CountManageRowItemViewHolder(private val binding: WishlistV2CountManageRowItemBinding,
                                             private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, isShowCheckbox: Boolean) {
        if (item.dataObject is WishlistV2CountManageRowData) {
            binding.wishlistCountLabel.text = "${item.dataObject.count}"
            binding.wishlistManageLabel.setOnClickListener {
                if (!isShowCheckbox) {
                    binding.wishlistManageLabel.text = itemView.context.getString(R.string.wishlist_cancel_manage_label)
                    actionListener?.onManageClicked(true)
                } else {
                    binding.wishlistManageLabel.text = itemView.context.getString(R.string.wishlist_manage_label)
                    actionListener?.onManageClicked(false)
                }
                item.dataObject.isBulkDeleteShow = !item.dataObject.isBulkDeleteShow
            }
        }
    }

    fun setManageLabel(label: String) {
        binding.wishlistManageLabel.text = label
    }
}