package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.databinding.WidgetReviewMediaImageThumbnailBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewMediaImageThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetReviewMediaImageThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val listener: ViewListeners = ViewListeners()
    private var uiState: ReviewMediaImageThumbnailUiState? = null

    init {
        binding.icReviewMediaImageThumbnailRemove.setOnClickListener(listener)
        binding.ivReviewMediaImageThumbnail.setOnClickListener(listener)
        binding.ivReviewMediaImageThumbnail.onUrlLoaded = { success ->
            when (uiState) {
                is ReviewMediaImageThumbnailUiState.Showing -> {
                    binding.reviewMediaImageThumbnailBrokenOverlay.showWithCondition(!success)
                    binding.icReviewMediaImageThumbnailBroken.showWithCondition(!success)
                }
                else -> {
                    binding.icReviewMediaImageThumbnailBroken.gone()
                    binding.reviewMediaImageThumbnailBrokenOverlay.showWithCondition(!success)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    private fun WidgetReviewMediaImageThumbnailBinding.setupImageThumbnail(
        uiState: ReviewMediaImageThumbnailUiState
    ) {
        ivReviewMediaImageThumbnail.setImageUrl(uiState.uri)
        icReviewMediaImageThumbnailRemove.showWithCondition(uiState.removable)
    }

    private fun WidgetReviewMediaImageThumbnailBinding.showImageThumbnail(
        uiState: ReviewMediaImageThumbnailUiState.Showing
    ) {
        groupReviewMediaImageThumbnailUploading.gone()
        groupReviewMediaImageThumbnailUploadFailed.gone()
        groupReviewMediaImageThumbnailSeeMore.gone()
        reviewMediaImageThumbnailBrokenOverlay.gone()
        icReviewMediaImageThumbnailBroken.gone()
        setupImageThumbnail(uiState)
    }

    private fun WidgetReviewMediaImageThumbnailBinding.showImageThumbnailSeeMore(
        uiState: ReviewMediaImageThumbnailUiState.ShowingSeeMore
    ) {
        groupReviewMediaImageThumbnailUploading.gone()
        groupReviewMediaImageThumbnailUploadFailed.gone()
        reviewMediaImageThumbnailBrokenOverlay.gone()
        icReviewMediaImageThumbnailBroken.gone()
        groupReviewMediaImageThumbnailSeeMore.show()
        tvReviewMediaImageThumbnailSeeMore.text = buildString {
            append("+")
            append(uiState.totalImageCount)
        }
        setupImageThumbnail(uiState)
    }

    private fun WidgetReviewMediaImageThumbnailBinding.showImageThumbnailUploadingState(
        uiState: ReviewMediaImageThumbnailUiState.Uploading
    ) {
        groupReviewMediaImageThumbnailUploadFailed.gone()
        groupReviewMediaImageThumbnailSeeMore.gone()
        reviewMediaImageThumbnailBrokenOverlay.gone()
        icReviewMediaImageThumbnailBroken.gone()
        groupReviewMediaImageThumbnailUploading.show()
        setupImageThumbnail(uiState)
    }

    private fun WidgetReviewMediaImageThumbnailBinding.showImageThumbnailUploadFailedState(
        uiState: ReviewMediaImageThumbnailUiState.UploadFailed
    ) {
        groupReviewMediaImageThumbnailUploading.gone()
        groupReviewMediaImageThumbnailSeeMore.gone()
        reviewMediaImageThumbnailBrokenOverlay.gone()
        icReviewMediaImageThumbnailBroken.gone()
        groupReviewMediaImageThumbnailUploadFailed.show()
        setupImageThumbnail(uiState)
    }

    fun updateUi(
        uiState: ReviewMediaImageThumbnailUiState
    ) {
        this.uiState = uiState
        when(uiState) {
            is ReviewMediaImageThumbnailUiState.Showing -> {
                binding.showImageThumbnail(uiState)
            }
            is ReviewMediaImageThumbnailUiState.ShowingSeeMore -> {
                binding.showImageThumbnailSeeMore(uiState)
            }
            is ReviewMediaImageThumbnailUiState.Uploading -> {
                binding.showImageThumbnailUploadingState(uiState)
            }
            is ReviewMediaImageThumbnailUiState.UploadFailed -> {
                binding.showImageThumbnailUploadFailedState(uiState)
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener.listener = newListener
    }

    private inner class ViewListeners: OnClickListener {
        var listener: Listener? = null
        override fun onClick(v: View?) {
            when(v) {
                binding.icReviewMediaImageThumbnailRemove -> {
                    listener?.onRemoveMediaItemClicked()
                }
                binding.ivReviewMediaImageThumbnail -> {
                    listener?.onMediaItemClicked()
                }
            }
        }
    }

    interface Listener {
        fun onMediaItemClicked()
        fun onRemoveMediaItemClicked()
    }
}