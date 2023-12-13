package com.tokopedia.content.product.preview.view.viewholder.product

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.data.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.databinding.ItemProductIndicatorBinding
import com.tokopedia.media.loader.loadImage

class ProductIndicatorViewHolder(
    private val binding: ItemProductIndicatorBinding
) : ViewHolder(binding.root) {

    fun bind(indicator: ProductIndicatorUiModel) {
        binding.ivReviewContentImage.loadImage(indicator.content.url)
        if (indicator.selected) {
            binding.viewSelected.background = ContextCompat.getDrawable(binding.root.context, R.drawable.product_indicator_selected_box)
        } else {
            binding.viewSelected.background = ContextCompat.getDrawable(binding.root.context, R.drawable.product_indicator_unselected_box)
        }
    }

}
