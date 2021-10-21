package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2LoaderListItemBinding
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2FilterRadioButtonViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2ListLoaderViewHolder
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

/**
 * Created by fwidjaja on 19/10/21.
 */
class WishlistV2FilterBottomSheetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: WishlistV2FilterBottomSheet.BottomSheetListener? = null
    var filterItem = WishlistV2Response.Data.WishlistV2.SortFiltersItem()

    companion object {
        const val LAYOUT_RADIO_BUTTON = 0
        const val LAYOUT_CHECKBOX = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_RADIO_BUTTON -> {
                val binding = BottomsheetWishlistFilterRadioButtonItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2FilterRadioButtonViewHolder(binding)
            }
            LAYOUT_CHECKBOX -> {
                // TODO : create loader grid
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistV2FilterRadioButtonViewHolder -> {
                holder.bind(holder.adapterPosition,
                        filterItem.options[holder.adapterPosition].text,
                        filterItem.options[holder.adapterPosition].optionId)
            }
        }
    }

    override fun getItemCount(): Int {
        return filterItem.options.size
    }

    fun setActionListener(listener: WishlistV2FilterBottomSheet.BottomSheetListener) {
        this.actionListener = listener
    }
}