package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.items.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.listener.MediaImageListener
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel

class ReviewMediaImageViewHolder(
    private val binding: ItemImageProductPreviewBinding,
    private val mediaImageLister: MediaImageListener
) : ViewHolder(binding.root) {

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                mediaImageLister.onImpressedImage()
            }

            override fun onViewDetachedFromWindow(p0: View) {
            }
        })
        binding.cvProductPreviewMediaImage.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool
        )
    }

    fun bind(content: ReviewMediaUiModel) {
        binding.cvProductPreviewMediaImage.apply {
            setContent {
                ItemImageProductPreview(
                    imageUrl = content.url,
                    onDoubleTap = { mediaImageLister.onDoubleTapImage() }
                )
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            mediaImageLister: MediaImageListener
        ) = ReviewMediaImageViewHolder(
            binding = ItemImageProductPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mediaImageLister = mediaImageLister
        )
    }
}
