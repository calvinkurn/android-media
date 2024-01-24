package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemProductIndicatorBinding
import com.tokopedia.content.product.preview.utils.millisToFormattedVideoDuration
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

internal class ProductIndicatorViewHolder(
    private val binding: ItemProductIndicatorBinding,
    private val listener: ProductIndicatorListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ProductContentUiModel) {
        binding.ivReviewContentImage.loadImage(data.thumbnailUrl)
        if (data.selected) {
            binding.viewSelected.background = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.product_indicator_selected_box
            )
        } else {
            binding.viewSelected.background = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.product_indicator_unselected_box
            )
        }

        binding.textVideoDuration.apply {
            showWithCondition(
                data.type == MediaType.Video && data.videoTotalDuration != 0L
            )
            text = data.videoTotalDuration.millisToFormattedVideoDuration(binding.root.context)
        }
        binding.root.setOnClickListener {
            listener.onClickProductIndicator(bindingAdapterPosition)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ProductIndicatorListener) =
            ProductIndicatorViewHolder(
                binding = ItemProductIndicatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener = listener
            )
    }
}
