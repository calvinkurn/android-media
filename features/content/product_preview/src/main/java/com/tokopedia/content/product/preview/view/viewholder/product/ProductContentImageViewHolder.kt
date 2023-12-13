package com.tokopedia.content.product.preview.view.viewholder.product

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.data.ContentUiModel
import com.tokopedia.content.product.preview.databinding.ItemProductContentImageBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class ProductContentImageViewHolder(
    private val binding: ItemProductContentImageBinding
) : ViewHolder(binding.root) {

    fun bind(content: ContentUiModel) {
        binding.ivProductContentImage.loadImage(content.url) {
            listener(
                onSuccess = { _, _ ->
                    hideLoading()
                },
                onError = { hideLoading() }
            )
        }
    }

    private fun hideLoading() {
        binding.apply {
            ivProductContentImage.show()
            loaderImage.hide()
        }
    }

}
