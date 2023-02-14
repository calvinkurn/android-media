package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterCheckboxItemBinding
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2FilterCheckboxViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2FilterRadioButtonViewHolder
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlistcommon.data.WishlistV2Params

/**
 * Created by fwidjaja on 19/10/21.
 */
class WishlistV2FilterBottomSheetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: WishlistV2FilterBottomSheet.BottomSheetListener? = null
    var filterItem = WishlistV2Response.Data.WishlistV2.SortFiltersItem()
    private val listFilterOffer = arrayListOf<WishlistV2Params.WishlistSortFilterParam>()
    var isResetCheckbox = false

    companion object {
        const val LAYOUT_RADIO_BUTTON = 0
        const val LAYOUT_CHECKBOX = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_RADIO_BUTTON -> {
                val binding = BottomsheetWishlistFilterRadioButtonItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2FilterRadioButtonViewHolder(binding, actionListener)
            }
            LAYOUT_CHECKBOX -> {
                val binding = BottomsheetWishlistFilterCheckboxItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2FilterCheckboxViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistV2FilterRadioButtonViewHolder -> {
                holder.bind(
                    filterItem.name,
                    filterItem.options[holder.adapterPosition].text,
                    filterItem.options[holder.adapterPosition].optionId,
                    filterItem.options[holder.adapterPosition].isSelected
                )
            }
            is WishlistV2FilterCheckboxViewHolder -> {
                holder.bind(
                    filterItem.name,
                    filterItem.options[holder.adapterPosition].text,
                    filterItem.options[holder.adapterPosition].description,
                    filterItem.options[holder.adapterPosition].optionId,
                    filterItem.options[holder.adapterPosition].isSelected,
                    isResetCheckbox
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (filterItem.selectionType) {
            1 -> LAYOUT_RADIO_BUTTON
            2 -> LAYOUT_CHECKBOX
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return filterItem.options.size
    }

    fun setActionListener(listener: WishlistV2FilterBottomSheet.BottomSheetListener) {
        this.actionListener = listener
    }

    private fun addFilterOffers(offerFilter: WishlistV2Params.WishlistSortFilterParam) {
        listFilterOffer.add(offerFilter)
    }
}
