package com.tokopedia.content.product.preview.view.viewholder.review

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.buildSpannedString
import com.tokopedia.content.common.util.doOnLayout
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemReviewContentBinding
import com.tokopedia.content.product.preview.utils.REVIEW_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.view.adapter.review.ReviewMediaAdapter
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewDescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ReviewContentViewHolder(
    private val binding: ItemReviewContentBinding,
    private val reviewInteractionListener: ReviewInteractionListener,
    private val mediaViewPool: RecyclerView.RecycledViewPool
) : ViewHolder(binding.root),
    ProductPreviewVideoListener {

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (reviewMediaAdapter.itemCount < 0) return
                binding.rvReviewMedia.addOnScrollListener(mediaScrollListener)
            }

            override fun onViewDetachedFromWindow(p0: View) {
                mVideoPlayer = null
                binding.rvReviewMedia.removeOnScrollListener(mediaScrollListener)
            }
        })
    }

    private val mediaScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
            val position = getContentCurrentPosition()
            binding.pcReviewContent.setCurrentIndicator(position)
        }
    }

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private val videoPlayerManager by lazyThreadSafetyNone {
        ProductPreviewVideoPlayerManager(binding.root.context)
    }

    private val reviewMediaAdapter: ReviewMediaAdapter by lazyThreadSafetyNone {
        ReviewMediaAdapter(
            productPreviewVideoListener = this
        )
    }

    private val layoutManagerMedia by lazyThreadSafetyNone {
        LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private var snapHelperMedia = PagerSnapHelper()

    private val descriptionUiModel = DescriptionUiModel()

    private val clickableSpan: ClickableSpan =
        object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color =
                    MethodChecker.getColor(
                        binding.root.context,
                        unifyprinciplesR.color.Unify_Static_White
                    )
                tp.isUnderlineText = false
                tp.typeface = Typeface.DEFAULT_BOLD
            }

            override fun onClick(widget: View) {
                descriptionUiModel.isExpanded = !descriptionUiModel.isExpanded
                setupExpanded()
            }
        }

    init {
        binding.tvReviewDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    fun bind(item: ReviewContentUiModel) {
        bindWatchMode(item.isWatchMode)
        bindMedia(item.medias, item.mediaSelectedPosition)
        bindAuthor(item.author)
        bindDescription(item.description)
        bindLike(item.likeState)
        setupTap(item)
    }

    fun onRecycled() {
        onMediaRecycled()
    }

    fun bindWatchMode(isWatchMode: Boolean) {
        binding.groupReviewDetails.showWithCondition(!isWatchMode)
        binding.groupReviewInteraction.showWithCondition(!isWatchMode)
        binding.icWatchMode.showWithCondition(isWatchMode)

        binding.icWatchMode.setOnClickListener {
            reviewInteractionListener.updateReviewWatchMode()
        }
    }

    private fun bindMedia(
        media: List<ReviewMediaUiModel>,
        mediaSelectedPosition: Int
    ) = with(binding.rvReviewMedia) {
        prepareVideoPlayerIfNeeded(media)

        adapter = reviewMediaAdapter
        layoutManager = layoutManagerMedia
        setHasFixedSize(true)
        addOnScrollListener(mediaScrollListener)
        setRecycledViewPool(mediaViewPool)
        snapHelperMedia.attachToRecyclerView(this)
        itemAnimator = null

        reviewMediaAdapter.submitList(media)

        setupPageControlMedia(media.size, mediaSelectedPosition)
        scrollToPosition(mediaSelectedPosition)
    }

    private fun onMediaRecycled() = with(binding.rvReviewMedia) {
        removeOnScrollListener(mediaScrollListener)
        reviewMediaAdapter.submitList(emptyList())
    }

    private fun bindAuthor(author: ReviewAuthorUiModel) = with(binding.layoutAuthorReview) {
        tvAuthorName.text = author.name
        ivAuthor.loadImageCircle(url = author.avatarUrl)
        lblAuthorStats.setLabel(author.type)
        lblAuthorStats.showWithCondition(author.type.isNotBlank())

        lblAuthorStats.setOnClickListener {
            reviewInteractionListener.onReviewCredibilityClicked(author)
        }
        tvAuthorName.setOnClickListener {
            reviewInteractionListener.onReviewCredibilityClicked(author)
        }
    }

    private fun bindDescription(description: ReviewDescriptionUiModel) = with(binding) {
        val divider = root.context.getString(R.string.circle_dot_divider)
        tvReviewDetails.text = buildString {
            append(description.stars)
            append(" $divider ")
            append(description.productType)
            append(" $divider ")
            append(description.timestamp)
        }
        setupReview(description.description)
    }

    private fun setupReview(description: String) = with(binding) {
        tvReviewDescription.invisible()
        tvReviewDescription.text = description
        tvReviewDescription.doOnLayout {
            val text = tvReviewDescription.layout
            if (text.lineCount <= MAX_LINES_THRESHOLD) return@doOnLayout

            val start = text.getLineStart(0)
            val end = text.getLineEnd(MAX_LINES_THRESHOLD - 1)

            val truncatedText = buildSpannedString {
                append(text.text.subSequence(start, end).dropLast(READ_MORE_COUNT))
                append(root.context.getString(R.string.product_prev_review_ellipsize))
                append(
                    root.context.getString(R.string.product_prev_review_expand),
                    clickableSpan,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }

            val ogText = buildSpannedString {
                append(description)
                append(root.context.getString(R.string.product_prev_review_ellipsize))
                append(
                    root.context.getString(R.string.product_prev_review_collapse),
                    clickableSpan,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            descriptionUiModel.originalText = ogText
            descriptionUiModel.truncatedText = truncatedText
            setupExpanded()
        }
        tvReviewDescription.show()
    }

    private fun setupExpanded() {
        val (lines, text) = if (descriptionUiModel.isExpanded) {
            Pair(MAX_LINES_VALUE, descriptionUiModel.originalText)
        } else {
            Pair(MAX_LINES_THRESHOLD, descriptionUiModel.truncatedText)
        }
        binding.tvReviewDescription.maxLines = lines
        binding.tvReviewDescription.text = text
    }

    fun bindLike(state: ReviewLikeUiState) = with(binding.layoutLikeReview) {
        val icon = when (state.state) {
            ReviewLikeUiState.ReviewLikeStatus.Reset, ReviewLikeUiState.ReviewLikeStatus.Dislike -> IconUnify.THUMB
            else -> IconUnify.THUMB_FILLED
        }
        ivReviewLike.setImage(newIconId = icon)
        tvLikeCount.text = state.count.toString()
        root.setOnClickListener {
            reviewInteractionListener.onLike(state.copy(withAnimation = false))
        }

        if (!state.withAnimation) return@with
        binding.ivDanceLike.onAnimStartAction = { binding.ivDanceLike.show() }
        binding.ivDanceLike.onAnimEndAction = { binding.ivDanceLike.gone() }
        binding.ivDanceLike.setIconEnabled(isEnabled = true)
        binding.ivDanceLike.setIsLiked(state.state == ReviewLikeUiState.ReviewLikeStatus.Like)
        binding.ivDanceLike.playLikeAnimation()
    }

    private fun setupTap(item: ReviewContentUiModel) {
        binding.ivReviewMenu.setOnClickListener {
            reviewInteractionListener.onMenuClicked()
        }

        val gesture = GestureDetector(
            binding.root.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    reviewInteractionListener.onLike(item.likeState.copy(withAnimation = true))
                    return true
                }
            }
        )
        binding.root.setOnTouchListener { _, motionEvent ->
            gesture.onTouchEvent(motionEvent)
            true
        }
    }

    private fun prepareVideoPlayerIfNeeded(media: List<ReviewMediaUiModel>) {
        if (mVideoPlayer != null) return

        media.forEachIndexed { index, reviewMediaUiModel ->
            if (reviewMediaUiModel.type != MediaType.Video) return@forEachIndexed
            val data = media[index]
            val videoUrl = data.url

            val instance = videoPlayerManager.occupy(
                String.format(
                    REVIEW_CONTENT_VIDEO_KEY_REF,
                    videoUrl
                )
            )
            val videoPlayer = mVideoPlayer ?: instance
            mVideoPlayer = videoPlayer
            mVideoPlayer?.start(
                videoUrl = videoUrl,
                isMute = false,
                playWhenReady = false
            )
        }
    }

    private fun setupPageControlMedia(
        mediaSize: Int,
        mediaSelectedPosition: Int
    ) = with(binding.pcReviewContent) {
        setIndicator(mediaSize)
        setCurrentIndicator(mediaSelectedPosition)
    }

    private fun getContentCurrentPosition(): Int {
        val snappedView = snapHelperMedia.findSnapView(layoutManagerMedia)
            ?: return RecyclerView.NO_POSITION
        return binding.rvReviewMedia.getChildAdapterPosition(snappedView)
    }

    override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
        return videoPlayerManager.occupy(id)
    }

    override fun pauseVideo(id: String) {
        videoPlayerManager.pause(id)
    }

    override fun resumeVideo(id: String) {
        videoPlayerManager.resume(id)
    }

    override fun onScrubbing() {
        binding.groupReviewDetails.hide()
        binding.groupReviewInteraction.hide()
    }

    override fun onStopScrubbing() {
        binding.groupReviewDetails.show()
        binding.groupReviewInteraction.show()
    }

    data class DescriptionUiModel(
        var isExpanded: Boolean = false,
        var originalText: SpannedString = buildSpannedString { },
        var truncatedText: SpannedString = buildSpannedString { }
    )

    companion object {
        private const val MAX_LINES_VALUE = 25
        private const val MAX_LINES_THRESHOLD = 2
        private const val READ_MORE_COUNT = 16

        fun create(
            parent: ViewGroup,
            reviewInteractionListener: ReviewInteractionListener,
            mediaViewPool: RecyclerView.RecycledViewPool
        ) = ReviewContentViewHolder(
            binding = ItemReviewContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            reviewInteractionListener = reviewInteractionListener,
            mediaViewPool = mediaViewPool
        )
    }
}
