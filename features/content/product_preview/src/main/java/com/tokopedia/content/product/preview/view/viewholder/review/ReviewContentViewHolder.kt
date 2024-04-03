package com.tokopedia.content.product.preview.view.viewholder.review

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.common.util.ContentItemComponentsAlphaAnimator
import com.tokopedia.content.common.util.changeConstraint
import com.tokopedia.content.product.preview.databinding.ItemReviewContentBinding
import com.tokopedia.content.product.preview.utils.REVIEW_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.view.adapter.review.ReviewMediaAdapter
import com.tokopedia.content.product.preview.view.components.ReviewCaptionView
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.MediaImageListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.listener.ReviewMediaListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewDescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState.ReviewLikeStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.content.product.preview.R

class ReviewContentViewHolder(
    private val binding: ItemReviewContentBinding,
    private val reviewInteractionListener: ReviewInteractionListener,
    private val reviewMediaListener: ReviewMediaListener,
    private val mediaViewPool: RecyclerView.RecycledViewPool
) : ViewHolder(binding.root),
    ProductPreviewVideoListener,
    MediaImageListener {

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (reviewMediaAdapter.itemCount < 0) return
                binding.rvReviewMedia.addOnScrollListener(mediaScrollListener)
                binding.ivDanceLike.gone()
                addLikeAnimationListener()
            }

            override fun onViewDetachedFromWindow(p0: View) {
                isShareAble = false
                mVideoPlayer = null
                binding.rvReviewMedia.removeOnScrollListener(mediaScrollListener)
                removeLikeAnimationListener()
            }
        })
    }

    private val alphaAnimator = ContentItemComponentsAlphaAnimator(object : ContentItemComponentsAlphaAnimator.Listener {
        override fun onAnimateAlpha(animator: ContentItemComponentsAlphaAnimator, alpha: Float) {
            opacityViewList.forEach { it.alpha = alpha }
        }
    })

    private val opacityViewList = listOf(
        binding.layoutAuthorReview.root,
        binding.layoutLikeReview.root,
        binding.ivReviewMenu,
        binding.ivReviewStar,
        binding.tvReviewDescription,
        binding.tvReviewDetails,
        binding.ivReviewShare,
    )

    private val mediaScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
            reviewInteractionListener.onReviewMediaScrolled()
            val position = getContentCurrentPosition()
            binding.pcReviewContent.setCurrentIndicator(position)
            reviewMediaListener.onMediaSelected(position)
        }
    }

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private val videoPlayerManager by lazyThreadSafetyNone {
        ProductPreviewVideoPlayerManager(binding.root.context)
    }

    private val reviewMediaAdapter: ReviewMediaAdapter by lazyThreadSafetyNone {
        ReviewMediaAdapter(
            productPreviewVideoListener = this,
            mediaImageLister = this
        )
    }

    private val layoutManagerMedia by lazyThreadSafetyNone {
        LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private var snapHelperMedia = PagerSnapHelper()

    private val captionViewListener = object : ReviewCaptionView.Listener {
        override fun onExpanded(view: ReviewCaptionView) {
            val screenHeight = getScreenHeight()
            val maxHeight = screenHeight * 0.45f
            binding.root.changeConstraint {
                constrainMaxHeight(binding.reviewOverlay.id, maxHeight.toInt())
            }
        }
    }

    private val captionView = ReviewCaptionView(
        binding.tvReviewDescription,
        binding.reviewOverlay,
        captionViewListener
    )

    private var isShareAble: Boolean = false

    init {
        binding.tvReviewDescription.movementMethod = LinkMovementMethod.getInstance()
        binding.layoutLikeReview.root.setOnClickListener {
            reviewInteractionListener.onLike(false)
        }
    }

    fun bind(item: ReviewContentUiModel) {
        bindScrolling(item.isScrolling)
        bindWatchMode(item.isWatchMode)
        bindMedia(item.medias, item.mediaSelectedPosition)
        bindAuthor(item.author)
        bindDescription(item.description)
        bindLike(item.likeState)
        bindShare(item.isShareAble)
        setupTap(item)
    }

    fun bindScrolling(isScrolling: Boolean) {
        onScrolling(isScrolling)
    }

    fun onRecycled() {
        onMediaRecycled()
    }

    fun bindWatchMode(isWatchMode: Boolean) {
        binding.groupReviewDetails.showWithCondition(!isWatchMode)
        binding.groupReviewInteraction.showWithCondition(!isWatchMode)

        // join ivReviewShare with groupReviewInteraction when share ab test is done
        binding.ivReviewShare.showWithCondition(!isWatchMode)

        binding.icWatchMode.showWithCondition(isWatchMode)

        binding.icWatchMode.setOnClickListener {
            reviewInteractionListener.updateReviewWatchMode()
        }
    }

    fun bindMediaDataChanged(mediaData: List<ReviewMediaUiModel>) {
        reviewMediaAdapter.submitList(mediaData)

        val position = mediaData.indexOfFirst { it.selected }
        val exactPosition = position.coerceAtLeast(0)
        scrollTo(exactPosition)
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

    private fun onScrolling(isScrolling: Boolean) {
        val startAlpha = opacityViewList.first().alpha
        if (isScrolling) {
            alphaAnimator.animateToAlpha(startAlpha)
        } else {
            alphaAnimator.animateToOpaque(startAlpha)
        }
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
        root.setOnClickListener {
            reviewInteractionListener.onReviewCredibilityClicked(author)
        }
    }

    private fun bindDescription(description: ReviewDescriptionUiModel) = with(binding) {
        val divider = root.context.getString(R.string.circle_dot_divider)
        tvReviewDetails.text = buildString {
            append(description.stars)
            if (description.productType.isNotBlank()) {
                append(" $divider ")
                append(description.productType)
            }
            append(" $divider ")
            append(description.timestamp)
        }
        captionView.bind(description.description)
    }

    fun bindLike(state: ReviewLikeUiState) {
        val icon = when (state.state) {
            ReviewLikeStatus.Reset, ReviewLikeStatus.Dislike -> IconUnify.THUMB
            else -> IconUnify.THUMB_FILLED
        }
        binding.layoutLikeReview.ivReviewLike.setImage(newIconId = icon)
        binding.layoutLikeReview.tvLikeCount.text = state.count.toString()

        if (!state.withAnimation) return
        binding.ivDanceLike.onAnimStartAction = { binding.ivDanceLike.show() }
        binding.ivDanceLike.onAnimEndAction = { binding.ivDanceLike.gone() }
        binding.ivDanceLike.setIconEnabled(isEnabled = true)
        binding.ivDanceLike.setIsLiked(state.state == ReviewLikeUiState.ReviewLikeStatus.Like)
        binding.ivDanceLike.playLikeAnimation()
    }

    private fun addLikeAnimationListener() {
        binding.ivDanceLike.addAnimationListeners()
    }

    private fun removeLikeAnimationListener() {
        binding.ivDanceLike.removeAllAnimationListeners()
    }

    private fun setupTap(item: ReviewContentUiModel) {
        binding.ivReviewMenu.setOnClickListener {
            reviewInteractionListener.onMenuClicked()
        }
        binding.ivReviewShare.setOnClickListener {
            reviewInteractionListener.onShareClicked(
                item = item,
                selectedMediaId = item.medias.getOrNull(getContentCurrentPosition())?.mediaId
                    ?: item.medias.firstOrNull()?.mediaId.orEmpty()
            )
        }
    }

    private fun prepareVideoPlayerIfNeeded(media: List<ReviewMediaUiModel>) {
        if (mVideoPlayer != null) return

        media.forEachIndexed { index, reviewMediaUiModel ->
            if (reviewMediaUiModel.type != MediaType.Video) return@forEachIndexed
            val data = media[index]
            val videoUrl = data.url

            val instance = videoPlayerManager.occupy(
                String.format(REVIEW_CONTENT_VIDEO_KEY_REF, videoUrl)
            )
            val videoPlayer = mVideoPlayer ?: instance
            mVideoPlayer = videoPlayer

            onStartVideoPlayer(videoUrl)
        }
    }

    private fun onStartVideoPlayer(videoUrl: String) {
        mVideoPlayer?.start(
            videoUrl = videoUrl,
            isMute = false,
            playWhenReady = false
        )
    }

    private fun setupPageControlMedia(
        mediaSize: Int,
        mediaSelectedPosition: Int
    ) = with(binding.pcReviewContent) {
        setIndicator(mediaSize)
        setCurrentIndicator(mediaSelectedPosition)
    }

    private fun bindShare(isUsingShare: Boolean) {
        isShareAble = isUsingShare // remove this when ab test is done
        binding.ivReviewShare.showWithCondition(isShareAble)
    }

    private fun scrollTo(position: Int) {
        binding.rvReviewMedia.smoothScrollToPosition(position)
        binding.pcReviewContent.setCurrentIndicator(position)
    }

    override fun onImpressedImage() {
        reviewMediaListener.onImpressedImage()
    }

    override fun onDoubleTapImage() {
        reviewInteractionListener.onLike(true)
    }

    override fun onDoubleTapVideo() {
        reviewInteractionListener.onLike(true)
    }

    override fun onImpressedVideo() {
        reviewMediaListener.onImpressedVideo()
    }

    private fun getContentCurrentPosition(): Int {
        val snappedView = snapHelperMedia.findSnapView(layoutManagerMedia)
            ?: return RecyclerView.NO_POSITION
        return binding.rvReviewMedia.getChildAdapterPosition(snappedView)
    }

    override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
        return videoPlayerManager.occupy(id)
    }

    override fun onPauseResumeVideo() {
        reviewInteractionListener.onPauseResumeVideo()
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
        binding.ivReviewShare.hide() // remove this when ab test is done
        binding.pcReviewContent.hide()
    }

    override fun onStopScrubbing() {
        binding.groupReviewDetails.show()
        binding.groupReviewInteraction.show()
        binding.ivReviewShare.showWithCondition(isShareAble) // remove this when ab test is done
        binding.pcReviewContent.show()
    }

    companion object {

        fun create(
            parent: ViewGroup,
            reviewInteractionListener: ReviewInteractionListener,
            reviewMediaListener: ReviewMediaListener,
            mediaViewPool: RecyclerView.RecycledViewPool
        ) = ReviewContentViewHolder(
            binding = ItemReviewContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            reviewInteractionListener = reviewInteractionListener,
            reviewMediaListener = reviewMediaListener,
            mediaViewPool = mediaViewPool
        )
    }
}
