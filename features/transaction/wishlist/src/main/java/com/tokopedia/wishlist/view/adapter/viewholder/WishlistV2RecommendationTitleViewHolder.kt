package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding

class WishlistV2RecommendationTitleViewHolder(private val binding: WishlistV2RecommendationTitleItemBinding, private val isWithMargin: Boolean) : RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val PADDING_20 = 20
        private const val PADDING_12 = 12
        private const val PADDING_10 = 10
    }

    fun bind(data: WishlistV2TypeLayoutData, isShowCheckbox: Boolean) {
        if (isShowCheckbox) {
            binding.root.gone()
            val params = LinearLayout.LayoutParams(0, 0)
            binding.root.layoutParams = params
        } else {
            binding.root.visible()
            if (isWithMargin) {
                binding.root.setPadding(PADDING_20.toPx(), PADDING_20.toPx(), 0, PADDING_12.toPx())
            } else {
                binding.root.setPadding(PADDING_10.toPx(), PADDING_20.toPx(), 0, PADDING_12.toPx())
            }
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            binding.root.layoutParams = params
            binding.tvRvTitleWishlistV2.text = data.dataObject as String
        }
    }
}