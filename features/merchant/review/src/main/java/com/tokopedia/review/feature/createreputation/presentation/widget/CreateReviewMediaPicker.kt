package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetCreateReviewMediaPickerBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewMediaTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPickerAddSmallViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPreviewImageViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPreviewVideoViewHolder
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewMediaPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewMediaPickerBinding>(context, attrs, defStyleAttr) {

    companion object {
        const val MAX_MEDIA_COUNT = 5
        private const val MEDIA_SPAN_SIZE_SMALL = 1
        private const val MEDIA_SPAN_SIZE_BIG = MAX_MEDIA_COUNT
    }

    private val mediaPickerListener = MediaPickerListener()
    private val layoutManager = LayoutManager(context)
    private val typeFactory = CreateReviewMediaTypeFactory(mediaPickerListener)
    private val adapter = CreateReviewMediaAdapter(typeFactory)
    private val transitionHandler = TransitionHandler()

    override val binding = WidgetCreateReviewMediaPickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutMediaPicker.root.layoutManager = layoutManager
        binding.layoutMediaPicker.root.adapter = adapter
        binding.layoutMediaPickerError.tvCreateReviewMediaPickerErrorUpload.text = HtmlLinkHelper(
            context,
            context.getString(R.string.review_form_media_picker_error_upload)
        ).spannedString ?: ""
        binding.layoutMediaPickerError.root.setOnClickListener(mediaPickerListener)
    }

    private fun showLoading() {
        transitionHandler.transitionToShowLoading()
    }

    private fun showMediaPickerUploadingState(
        uiState: CreateReviewMediaPickerUiState.Uploading
    ) {
        val successCount = uiState
            .mediaItems
            .filter {
                (it.isImage() || it.isVideo()) && it.uploadBatchNumber == uiState.currentUploadBatchNumber
            }
            .count { it.state == CreateReviewMediaUiModel.State.UPLOADED }
        transitionHandler.transitionToShowMediaPicker(showError = false, showPoem = true)
        setupMediaPicker(uiState.mediaItems)
        setupWaitingState(uiState.poem, successCount)
    }

    private fun showMediaPickerSuccessUploadState(
        uiState: CreateReviewMediaPickerUiState.SuccessUpload
    ) {
        transitionHandler.transitionToShowMediaPicker(showError = false, showPoem = uiState.poem.id.isMoreThanZero())
        setupMediaPicker(uiState.mediaItems)
        setupWaitingState(uiState.poem)
    }

    private fun showMediaPickerFailedUploadState(
        uiState: CreateReviewMediaPickerUiState.FailedUpload
    ) {
        transitionHandler.transitionToShowMediaPicker(showError = true, showPoem = false)
        setupMediaPicker(uiState.mediaItems)
    }

    private fun setupMediaPicker(
        mediaItems: List<CreateReviewMediaUiModel>
    ) {
        adapter.setMediaReviewData(mediaItems)
    }

    private fun setupWaitingState(poem: StringRes, successCount: Int = Int.ZERO) {
        val waitingText = when (poem.id) {
            R.string.review_form_on_progress_upload_poem -> {
                poem.getStringValueWithCustomParam(context, successCount)
            }
            else -> {
                poem.getStringValue(context)
            }
        }
        binding.layoutMediaPickerWaitingState.tvCreateReviewMediaPickerPoem.text = waitingText
    }

    fun updateUi(uiState: CreateReviewMediaPickerUiState, continuation: Continuation<Unit>) {
        when(uiState) {
            is CreateReviewMediaPickerUiState.Loading -> {
                showLoading()
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewMediaPickerUiState.Uploading -> {
                showMediaPickerUploadingState(uiState)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewMediaPickerUiState.SuccessUpload -> {
                showMediaPickerSuccessUploadState(uiState)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewMediaPickerUiState.FailedUpload -> {
                showMediaPickerFailedUploadState(uiState)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }

    fun setListener(newCreateReviewMediaPickerListener: Listener) {
        mediaPickerListener.listener = newCreateReviewMediaPickerListener
    }

    private inner class LayoutManager(context: Context): GridLayoutManager(context, MAX_MEDIA_COUNT, VERTICAL, false) {

        private val spanSizeLookup = SpanSizeLookup()

        init {
            setSpanSizeLookup(spanSizeLookup)
        }

        private inner class SpanSizeLookup: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(adapter.getItemViewType(position)) {
                    CreateReviewMediaPreviewImageViewHolder.LAYOUT,
                    CreateReviewMediaPreviewVideoViewHolder.LAYOUT,
                    CreateReviewMediaPickerAddSmallViewHolder.LAYOUT -> MEDIA_SPAN_SIZE_SMALL
                    else -> MEDIA_SPAN_SIZE_BIG
                }
            }
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutMediaPicker.root)
                addTarget(binding.layoutMediaPickerError.root)
                addTarget(binding.layoutMediaPickerLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewMediaPickerBinding.showLoadingLayout() {
            layoutMediaPickerLoading.root.show()
        }

        private fun WidgetCreateReviewMediaPickerBinding.hideLoadingLayout() {
            layoutMediaPickerLoading.root.gone()
        }

        private fun WidgetCreateReviewMediaPickerBinding.showMediaPickerLayout(
            showError: Boolean,
            showPoem: Boolean
        ) {
            layoutMediaPicker.root.show()
            layoutMediaPickerError.root.showWithCondition(showError)
            layoutMediaPickerWaitingState.root.showWithCondition(showPoem)
        }

        private fun WidgetCreateReviewMediaPickerBinding.hideMediaPickerLayout() {
            layoutMediaPicker.root.gone()
            layoutMediaPickerError.root.gone()
            layoutMediaPickerWaitingState.root.gone()
        }

        private fun WidgetCreateReviewMediaPickerBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowMediaPicker(showError: Boolean, showPoem: Boolean) {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showMediaPickerLayout(showError, showPoem)
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideMediaPickerLayout()
                showLoadingLayout()
            }
        }
    }

    private inner class MediaPickerListener: CreateReviewMediaAdapter.Listener, OnClickListener {
        var listener: Listener? = null

        override fun onAddMediaClicked(enabled: Boolean) {
            listener?.onAddMediaClicked(enabled)
        }

        override fun onRemoveMediaClicked(media: CreateReviewMediaUiModel) {
            listener?.onRemoveMediaClicked(media)
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                binding.layoutMediaPickerError.root.id -> listener?.onRetryUploadClicked()
            }
        }
    }

    interface Listener {
        fun onAddMediaClicked(enabled: Boolean)
        fun onRemoveMediaClicked(media: CreateReviewMediaUiModel)
        fun onRetryUploadClicked()
    }
}