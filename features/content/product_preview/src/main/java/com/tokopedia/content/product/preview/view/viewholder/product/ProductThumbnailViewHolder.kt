package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemProductThumbnailBinding
import com.tokopedia.content.product.preview.utils.millisToFormattedVideoDuration
import com.tokopedia.content.product.preview.view.listener.ProductThumbnailListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

internal class ProductThumbnailViewHolder(
    private val binding: ItemProductThumbnailBinding,
    private val listener: ProductThumbnailListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ProductMediaUiModel) {
        binding.ivReviewMediaImage.loadImage(data.thumbnailUrl)
        if (data.selected) {
            binding.viewSelected.background = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.product_thumbnail_selected_box
            )
        } else {
            binding.viewSelected.background = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.product_thumbnail_unselected_box
            )
        }

        binding.textVideoDuration.apply {
            showWithCondition(
                data.type == MediaType.Video && data.videoTotalDuration != 0L
            )
            text = data.videoTotalDuration.millisToFormattedVideoDuration(binding.root.context)
        }
        binding.root.setOnClickListener {
            listener.onClickProductThumbnail(bindingAdapterPosition)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ProductThumbnailListener) =
            ProductThumbnailViewHolder(
                binding = ItemProductThumbnailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener = listener
            )
    }
}
