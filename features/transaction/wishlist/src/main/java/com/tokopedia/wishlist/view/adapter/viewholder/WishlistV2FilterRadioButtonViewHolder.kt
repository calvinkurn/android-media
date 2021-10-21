package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding

/**
 * Created by fwidjaja on 20/10/21.
 */
class WishlistV2FilterRadioButtonViewHolder(private val binding: BottomsheetWishlistFilterRadioButtonItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(adapterPosition: Int, label: String, optionId: String) {
        binding.labelOption.text = label

        // make sure lagi onclick nya nanti kayak gimana hmm --> string saja kesatuan kah, atau perlu bawa name/text kah
    }
}