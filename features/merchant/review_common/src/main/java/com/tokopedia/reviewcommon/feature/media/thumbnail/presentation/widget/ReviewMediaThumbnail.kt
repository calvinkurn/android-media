package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.reviewcommon.databinding.WidgetReviewMediaThumbnailBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.ReviewMediaThumbnailAdapter
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewMediaThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_MEDIA_COUNT = 5
    }

    private val binding = WidgetReviewMediaThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val reviewMediaThumbnailListener = ReviewMediaThumbnailItemListener()
    private val typeFactory = ReviewMediaThumbnailTypeFactory(reviewMediaThumbnailListener)
    private val adapter = ReviewMediaThumbnailAdapter(typeFactory)

    init {
        binding.root.layoutManager = ReviewMediaThumbnailLayoutManager(context)
        binding.root.adapter = adapter
    }

    fun setData(data: ReviewMediaThumbnailUiModel) {
        adapter.updateItems(data.mediaThumbnails.take(MAX_MEDIA_COUNT))
    }

    fun setListener(listener: Listener) {
        reviewMediaThumbnailListener.listener = listener
    }

    fun setRecyclerViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        binding.root.setRecycledViewPool(recycledViewPool)
    }

    private inner class ReviewMediaThumbnailLayoutManager(
        context: Context
    ) : GridLayoutManager(context, MAX_MEDIA_COUNT, VERTICAL, false) {
        private val spanSizeLookup = ReviewMediaThumbnailSpanSizeLookup()

        init {
            setSpanSizeLookup(getSpanSizeLookup())
        }

        override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
            super.setSpanSizeLookup(getSpanSizeLookup())
        }

        override fun getSpanSizeLookup(): SpanSizeLookup {
            return spanSizeLookup
        }

        private inner class ReviewMediaThumbnailSpanSizeLookup : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
    }

    private inner class ReviewMediaThumbnailItemListener : ReviewMediaThumbnailTypeFactory.Listener {
        var listener: Listener? = null

        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            listener?.onMediaItemClicked(item, position)
        }

        override fun onRemoveMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            listener?.onRemoveMediaItemClicked(item, position)
        }
    }

    interface Listener {
        fun onMediaItemClicked(mediaItem: ReviewMediaThumbnailVisitable, position: Int)
        fun onRemoveMediaItemClicked(mediaItem: ReviewMediaThumbnailVisitable, position: Int)
    }
}