package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.databinding.ItemProductContentImageBinding
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class ProductContentImageViewHolder(
    private val binding: ItemProductContentImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: ProductContentUiModel) {
        showLoading()
        renderImage(content.url)
    }

    internal fun onRecycled() {
        binding.ivProductContentImage.clearImage()
    }

    private fun renderImage(imageUrl: String) = with(binding.ivProductContentImage) {
        loadImageWithoutPlaceholder(imageUrl) {
            listener(
                onSuccess = { _, _ -> hideLoading() },
                onError = { showErrorImage() }
            )
        }
    }

    private fun showLoading() {
        binding.apply {
            ivProductContentImage.invisible()
            loaderImage.show()
        }
    }

    private fun hideLoading() {
        binding.apply {
            ivProductContentImage.show()
            loaderImage.invisible()
        }
    }

    private fun showErrorImage() {
        binding.apply {
            ivProductContentImage.apply {
                clearImage()
                show()
            }
            loaderImage.invisible()
        }
    }

    companion object {
        fun create(parent: ViewGroup) =
            ProductContentImageViewHolder(
                binding = ItemProductContentImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}
