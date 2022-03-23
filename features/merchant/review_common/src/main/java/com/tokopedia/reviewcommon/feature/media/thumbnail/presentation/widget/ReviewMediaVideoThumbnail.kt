package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.WidgetReviewMediaVideoThumbnailBinding
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class ReviewMediaVideoThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr), CoroutineScope {

    private val binding = WidgetReviewMediaVideoThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val listener: ViewListeners = ViewListeners()
    private val job = SupervisorJob()
    private var uiState: ReviewMediaVideoThumbnailUiState? = null

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    init {
        binding.icReviewMediaVideoThumbnailPlayButton.loadImage(R.drawable.ic_review_media_video_thumbnail_play)
        binding.ivReviewMediaVideoThumbnail.onUrlLoaded = { success ->
            when (uiState) {
                is ReviewMediaVideoThumbnailUiState.Showing -> {
                    binding.reviewMediaVideoThumbnailBrokenOverlay.showWithCondition(!success)
                    binding.icReviewMediaVideoThumbnailBroken.showWithCondition(!success)
                }
                else -> {
                    binding.reviewMediaVideoThumbnailBrokenOverlay.showWithCondition(!success)
                    binding.icReviewMediaVideoThumbnailBroken.gone()
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancelChildren()
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.setupVideoThumbnail(
        uiState: ReviewMediaVideoThumbnailUiState
    ) {
        ivReviewMediaVideoThumbnail.setImageUrl(uiState.uri)
        icReviewMediaVideoThumbnailRemove.showWithCondition(uiState.removable)
        icReviewMediaVideoThumbnailPlayButton.showWithCondition(
            uiState is ReviewMediaVideoThumbnailUiState.Showing && uiState.playable
        )
        tvReviewMediaVideoThumbnailDuration.showWithCondition(uiState.showDuration)
        if (uiState.showDuration) {
            launchCatchError(block = {
                withContext(Dispatchers.IO) {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(uiState.uri)
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                }.let { durationMillis ->
                    val minute = TimeUnit.MILLISECONDS.toMinutes(durationMillis.toLongOrZero())
                    val minuteInMillis = TimeUnit.MINUTES.toMillis(minute)
                    val second = TimeUnit.MILLISECONDS.toSeconds(
                        (durationMillis.toLongOrZero() - minuteInMillis).coerceAtLeast(0L)
                    )
                    withContext(Dispatchers.Main) {
                        if (minute.isMoreThanZero() || second.isMoreThanZero()) {
                            tvReviewMediaVideoThumbnailDuration.run {
                                text = buildString {
                                    append(minute)
                                    append(":")
                                    append(second)
                                }
                                show()
                            }
                        } else {
                            tvReviewMediaVideoThumbnailDuration.gone()
                        }
                    }
                }
            }, onError = {})
        }
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnail(
        uiState: ReviewMediaVideoThumbnailUiState.Showing
    ) {
        groupReviewMediaVideoThumbnailSeeMore.gone()
        groupReviewMediaVideoThumbnailUploading.gone()
        groupReviewMediaVideoThumbnailUploadFailed.gone()
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        setupVideoThumbnail(uiState)
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnailSeeMore(
        uiState: ReviewMediaVideoThumbnailUiState.ShowingSeeMore
    ) {
        groupReviewMediaVideoThumbnailUploading.gone()
        groupReviewMediaVideoThumbnailUploadFailed.gone()
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        groupReviewMediaVideoThumbnailSeeMore.show()
        tvReviewMediaVideoThumbnailSeeMore.text = buildString {
            append("+")
            append(uiState.totalImageCount)
        }
        setupVideoThumbnail(uiState)
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnailUploadingState(
        uiState: ReviewMediaVideoThumbnailUiState.Uploading
    ) {
        groupReviewMediaVideoThumbnailSeeMore.gone()
        groupReviewMediaVideoThumbnailUploadFailed.gone()
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        groupReviewMediaVideoThumbnailUploading.show()
        setupVideoThumbnail(uiState)
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnailUploadFailedState(
        uiState: ReviewMediaVideoThumbnailUiState.UploadFailed
    ) {
        groupReviewMediaVideoThumbnailSeeMore.gone()
        groupReviewMediaVideoThumbnailUploading.gone()
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        groupReviewMediaVideoThumbnailUploadFailed.show()
        setupVideoThumbnail(uiState)
    }

    fun updateUi(uiState: ReviewMediaVideoThumbnailUiState) {
        this.uiState = uiState
        job.cancelChildren()
        when (uiState) {
            is ReviewMediaVideoThumbnailUiState.Showing -> {
                binding.showVideoThumbnail(uiState)
            }
            is ReviewMediaVideoThumbnailUiState.ShowingSeeMore -> {
                binding.showVideoThumbnailSeeMore(uiState)
            }
            is ReviewMediaVideoThumbnailUiState.Uploading -> {
                binding.showVideoThumbnailUploadingState(uiState)
            }
            is ReviewMediaVideoThumbnailUiState.UploadFailed -> {
                binding.showVideoThumbnailUploadFailedState(uiState)
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener.listener = newListener
    }

    private inner class ViewListeners : OnClickListener {
        var listener: Listener? = null
        override fun onClick(v: View?) {
            when (v) {
                binding.icReviewMediaVideoThumbnailRemove -> {
                    listener?.onRemoveMediaItemClicked()
                }
                binding.ivReviewMediaVideoThumbnail -> {
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