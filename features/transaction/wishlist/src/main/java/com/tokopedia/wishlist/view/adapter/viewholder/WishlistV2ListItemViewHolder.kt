package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2ListItemViewHolder(private val binding: WishlistV2ListItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int, isShowCheckbox: Boolean) {
        binding.wishlistItem.setProductModel(item.dataObject as ProductCardModel)
        if (isShowCheckbox) {
            binding.wishlistCheckbox.setOnCheckedChangeListener(null)
            binding.wishlistCheckbox.visible()
            binding.wishlistCheckbox.isChecked = item.isChecked
            binding.wishlistCheckbox.setOnCheckedChangeListener(checkboxListener(item, position))

            binding.wishlistItem.setSecondaryButtonVisibility(false)
            binding.wishlistItem.setPrimaryButtonVisibility(false)
        } else {
            binding.wishlistCheckbox.gone()
            binding.wishlistItem.setSecondaryButtonVisibility(true)
            binding.wishlistItem.setPrimaryButtonVisibility(true)
        }

        if (item.dataObject.tambahKeranjangButton) {
            binding.wishlistItem.setTambahKeranjangButtonClickListener { actionListener?.onAtc(item.wishlistItem) }
        } else {
            binding.wishlistItem.setLihatBarangSerupaButtonClickListener {
                actionListener?.onCheckSimilarProduct(
                    item.wishlistItem.buttons.primaryButton.url
                )
            }
        }

        binding.wishlistItem.setSecondaryButtonClickListener {
            actionListener?.onThreeDotsMenuClicked(
                item.wishlistItem
            )
        }

        binding.wishlistItem.setOnClickListener {
            actionListener?.onProductItemClicked(item.wishlistItem.id)
        }
    }

    private fun checkboxListener(item: WishlistV2TypeLayoutData, position: Int): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            actionListener?.onCheckBulkDeleteOption(item.wishlistItem.id, isChecked, position)
        }
    }
}