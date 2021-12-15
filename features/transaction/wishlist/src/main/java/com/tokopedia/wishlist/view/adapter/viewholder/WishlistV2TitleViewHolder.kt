package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding

class WishlistV2TitleViewHolder(private val binding: WishlistV2RecommendationTitleItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: WishlistV2TypeLayoutData, isShowCheckbox: Boolean) {
        if (isShowCheckbox) {
            binding.root.gone()
            val params = LinearLayout.LayoutParams(0, 0)
            binding.root.layoutParams = params
        } else {
            binding.root.visible()
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            binding.root.layoutParams = params
            binding.tvRvTitleWishlistV2.text = data.dataObject as String
        }
    }
}