package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.databinding.ItemProductContentImageBinding
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class ProductContentImageViewHolder(
    private val binding: ItemProductContentImageBinding,
    private val listener: ProductPreviewListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: ContentUiModel) {
        binding.ivProductContentImage.loadImageWithoutPlaceholder(content.url) {
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

    companion object {
        fun create(parent: ViewGroup, listener: ProductPreviewListener) =
            ProductContentImageViewHolder(
                binding = ItemProductContentImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener = listener
            )
    }
}
