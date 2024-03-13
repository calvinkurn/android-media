package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.items.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.listener.MediaImageListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import timber.log.Timber

class ProductMediaImageViewHolder(
    private val binding: ItemImageProductPreviewBinding,
    private val mediaImageLister: MediaImageListener
) : RecyclerView.ViewHolder(binding.root) {

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

    fun bind(content: ProductMediaUiModel) {
        val gesture = GestureDetector(
            binding.root.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    Timber.d("${e.pointerCount}")
                    return true
                }
            }
        )
        binding.cvProductPreviewMediaImage.apply {
            setOnTouchListener { _, motionEvent ->
                performClick()
                gesture.onTouchEvent(motionEvent)
                true
            }
        }
        binding.cvProductPreviewMediaImage.apply {
            setContent {
                ItemImageProductPreview(
                    imageUrl = content.url,
                    stateListener = { isScalingMode ->
                        mediaImageLister.onImageInteraction(isScalingMode)
                    }
                )
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            mediaImageLister: MediaImageListener
        ) = ProductMediaImageViewHolder(
            binding = ItemImageProductPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mediaImageLister = mediaImageLister
        )
    }
}
