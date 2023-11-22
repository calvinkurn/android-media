package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val reviewMediaThumbnailListener = ReviewMediaThumbnailListener()
    private val typeFactory by lazy(LazyThreadSafetyMode.NONE) {
        ReviewMediaThumbnailTypeFactory(reviewMediaThumbnailListener)
    }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ReviewMediaThumbnailAdapter(typeFactory)
    }

    init {
//        binding.root.layoutManager = ReviewMediaThumbnailLayoutManager(context)
        binding.root.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setData(data: ReviewMediaThumbnailUiModel) {
        if (binding.root.adapter == null) {
            binding.root.adapter = adapter
        }
        adapter.setElements(data.mediaThumbnails.take(MAX_MEDIA_COUNT))
    }

    fun setListener(listener: ReviewMediaThumbnailTypeFactory.Listener) {
        reviewMediaThumbnailListener.listener = listener
    }

    fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        (binding.root.layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
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

    private inner class ReviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener {
        var listener: ReviewMediaThumbnailTypeFactory.Listener? = null
        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            listener?.onMediaItemClicked(item, position)
        }
    }
}
