package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistStorageCleanerItemBinding

class WishlistV2CleanerBottomSheetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var wishlistItem = WishlistV2Response.Data.WishlistV2.Item()
    var cleanerBottomSheet = WishlistV2UiModel.StorageCleanerBottomSheet()
    private var selectedOption = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetWishlistStorageCleanerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistV2CleanerOptionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistV2CleanerOptionItemViewHolder -> {
                holder.bind(cleanerBottomSheet.options[holder.adapterPosition],
                    holder.adapterPosition == (cleanerBottomSheet.options.size-1), holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return cleanerBottomSheet.options.size
    }

    inner class WishlistV2CleanerOptionItemViewHolder(private val binding: BottomsheetWishlistStorageCleanerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            optionCleanerItem: WishlistV2UiModel.StorageCleanerBottomSheet.OptionCleanerBottomsheet,
            isHideDivider: Boolean,
            adapterPosition: Int
        ) {
            binding.wishlistCleanerOptionTitle.text = MethodChecker.fromHtml(optionCleanerItem.name)
            binding.wishlistCleanerOptionDesc.text = MethodChecker.fromHtml(optionCleanerItem.description)

            if (isHideDivider) binding.wishlistCleanerDivider.gone()
            else binding.wishlistCleanerDivider.visible()

            if (selectedOption == -1) {
                if (adapterPosition == 0) binding.wishlistCleanerOptionIconCheck.visible()
                else binding.wishlistCleanerOptionIconCheck.gone()
            } else {
                binding.wishlistCleanerOptionIconCheck.visibility = if (selectedOption == adapterPosition) View.VISIBLE else View.GONE
            }

            binding.clCleanerItem.setOnClickListener {
                selectItem(adapterPosition)
            }
        }
    }

    private fun selectItem(position: Int) {
        selectedOption = position
        notifyDataSetChanged()
    }

    fun getSelectedIndex(): Int {
        return selectedOption
    }
}