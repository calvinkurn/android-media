package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_COMMENT_COUNT
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_DONE_SCROLL
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_REMINDER_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SCROLLING
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SCROLLING_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloads
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.customview.FeedPlayerControl
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.*
import com.tokopedia.feedplus.presentation.util.animation.FeedLikeAnimationComponent
import com.tokopedia.feedplus.presentation.util.animation.FeedPostAlphaAnimator
import com.tokopedia.feedplus.presentation.util.animation.FeedSmallLikeIconAnimationComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.util.extension.changeConstraint

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
@SuppressLint("ClickableViewAccessibility")
class FeedPostVideoViewHolder(
    private val binding: ItemFeedPostVideoBinding,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardVideoContentModel>(binding.root) {

    private val alphaAnimator = FeedPostAlphaAnimator(object : FeedPostAlphaAnimator.Listener {
        override fun onAnimateAlpha(animator: FeedPostAlphaAnimator, alpha: Float) {
            opacityViewList.forEach { it.alpha = alpha }
        }
    })

    private val captionViewListener = object : FeedCaptionView.Listener {
        override fun onExpanded(view: FeedCaptionView) {
            val screenHeight = getScreenHeight()
            val maxHeight = screenHeight * 0.45f
            binding.root.changeConstraint {
                constrainMaxHeight(binding.layoutFeedCaption.id, maxHeight.toInt())
            }
        }
    }

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(
        binding.tvFeedCaption,
        binding.layoutFeedCaption,
        listener,
        captionViewListener
    )
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView = FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(binding.root)
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)
    private val commentButtonView = FeedCommentButtonView(binding.feedCommentButton, listener)

    private var mVideoPlayer: FeedExoPlayer? = null
    private var mIsSelected: Boolean = false
    private var mData: FeedCardVideoContentModel? = null

    private var trackerDataModel: FeedTrackerDataModel? = null

    private val opacityViewList = listOf(
        binding.layoutAuthorInfo.root,
        binding.tvFeedCaption,
        binding.postLikeButton.root,
        binding.feedCommentButton.root,
        binding.menuButton,
        binding.shareButton,
        binding.productTagButton.root,
        binding.productTagView.root,
        binding.overlayTop.root,
        binding.overlayBottom.root,
        binding.overlayRight.root,
        binding.btnDisableClearMode,
        binding.productTagView.root,
        binding.productTagButton.root,
        binding.rvFeedAsgcTags,
        binding.feedCampaignRibbon.root
    )

    init {
        binding.root.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {}

            override fun onViewDetachedFromWindow(view: View) {
                onNotSelected()
            }
        })

        binding.playerControl.setListener(object : FeedPlayerControl.Listener {
            override fun onScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                binding.videoTimeView.setCurrentPosition(currPosition)
                binding.videoTimeView.setTotalDuration(totalDuration)
                binding.videoTimeView.show()
                showClearView(showDisableClearMode = false)
            }

            override fun onStopScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                binding.videoTimeView.hide()
                hideClearView()
                mData?.let {
                    listener.onTapHoldSeekbarVideoPost(
                        trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                            it
                        )
                    )
                }
            }
        })

        binding.postLikeButton.likeButton.setOnClickListener {
            val data = mData ?: return@setOnClickListener
            listener.onLikePostCLicked(
                data.id,
                absoluteAdapterPosition,
                trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                    data
                ),
                false
            )
        }

        val postGestureDetector = GestureDetector(
            binding.root.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    val videoPlayer = mVideoPlayer ?: return true
                    val data = mData ?: return true

                    videoPlayer.getExoPlayer().playWhenReady =
                        !videoPlayer.getExoPlayer().playWhenReady

                    if (!videoPlayer.getExoPlayer().playWhenReady) {
                        listener.onPauseVideoPost(
                            trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                                data
                            )
                        )
                    }
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val data = mData ?: return true
                    if (data.like.isLiked.not()) {
                        listener.onLikePostCLicked(
                            data.id,
                            absoluteAdapterPosition,
                            trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                                data
                            ),
                            true
                        )
                    }
                    return true
                }

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                }
            }
        )

        binding.playerFeedVideo.videoSurfaceView?.setOnTouchListener { _, motionEvent ->
            postGestureDetector.onTouchEvent(motionEvent)
        }
    }

    fun bind(item: FeedContentAdapter.Item) {
        val data = item.data as FeedCardVideoContentModel
        bind(data)

        if (item.isSelected) {
            onSelected(data)
        } else {
            onNotSelected()
        }
    }

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            mData = data
            trackerDataModel = trackerMapper.transformVideoContentToTrackerModel(data)

            with(binding) {
                bindAuthor(data)
                bindCaption(data)
                bindProductTag(data)
                bindLike(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)
                bindComments(data)
                bindVideoPlayer(data)

                val trackerData =
                    trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(
                        data.id,
                        data.menuItems,
                        trackerData
                    )
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(data.share, trackerData)
                }
                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }
            }
        }
    }

    fun bind(item: FeedContentAdapter.Item, payloads: MutableList<Any>) {
        val selectedPayload = if (item.isSelected) FEED_POST_SELECTED else FEED_POST_NOT_SELECTED
        val scrollingPayload = if (item.isScrolling) FEED_POST_SCROLLING else FEED_POST_DONE_SCROLL

        val feedPayloads =
            payloads.firstOrNull { it is FeedViewHolderPayloads } as? FeedViewHolderPayloads

        if (feedPayloads == null) {
            bind(item.data as FeedCardVideoContentModel, payloads)
        } else {
            val newPayloads = mutableListOf(*payloads.toTypedArray()).apply {
                if (feedPayloads.payloads.contains(FEED_POST_SELECTED_CHANGED)) add(selectedPayload)
                if (feedPayloads.payloads.contains(FEED_POST_SCROLLING_CHANGED)) add(scrollingPayload)
            }
            bind(item.data as FeedCardVideoContentModel, newPayloads)
        }
    }

    override fun bind(element: FeedCardVideoContentModel?, payloads: MutableList<Any>) {
        mData = element
        element?.let {
            trackerDataModel = trackerMapper.transformVideoContentToTrackerModel(it)

            if (payloads.contains(FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(it.like)
            }

            if (payloads.contains(FEED_POST_CLEAR_MODE)) {
                showClearView()
            }

            if (payloads.contains(FEED_POST_COMMENT_COUNT)) {
                bindComments(it)
            }

            if (payloads.contains(FEED_POST_REMINDER_CHANGED)) {
                campaignView.bindCampaignReminder(element.campaign.isReminderActive)
            }

            if (payloads.contains(FEED_POST_SELECTED)) {
                onSelected(element)
            }

            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                onNotSelected()
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_FOLLOW_CHANGED)) {
                bindAuthor(element)
            }

            if (payloads.contains(FEED_POST_SCROLLING)) {
                onScrolling(true)
            }

            if (payloads.contains(FEED_POST_DONE_SCROLL)) {
                onScrolling(false)
            }

            payloads.forEach { payload ->
                if (payload is FeedViewHolderPayloads) {
                    bind(
                        element,
                        payload.payloads.toMutableList()
                    )
                }
            }
        }
    }

    private fun renderLikeView(
        like: FeedLikeModel
    ) {
        val isLiked = like.isLiked
        likeAnimationView.setEnabled(isEnabled = true)
        smallLikeAnimationView.setEnabled(isEnabled = true)

        likeAnimationView.setIsLiked(true)
        binding.postLikeButton.likedText.text = like.countFmt
        if (isLiked) {
            likeAnimationView.show()
        } else {
            likeAnimationView.hide()
        }
    }

    private fun setLikeUnlike(like: FeedLikeModel) {
        val isLiked = like.isLiked
        renderLikeView(like)
        if (isLiked) {
            likeAnimationView.playLikeAnimation()
            smallLikeAnimationView.playLikeAnimation()
        } else {
            smallLikeAnimationView.playUnLikeAnimation()
        }
    }

    private fun bindAuthor(data: FeedCardVideoContentModel) {
        authorView.bindData(data.author, false, !data.followers.isFollowed, trackerDataModel)
    }

    private fun bindCaption(data: FeedCardVideoContentModel) {
        captionView.bind(data.text, trackerDataModel)
    }

    private fun bindLike(data: FeedCardVideoContentModel) {
        val like = data.like
        binding.postLikeButton.likedText.text = like.countFmt
        smallLikeAnimationView.setIsLiked(like.isLiked)
    }

    private fun bindProductTag(data: FeedCardVideoContentModel) {
        productTagView.bindData(
            postId = data.id,
            author = data.author,
            postType = data.typename,
            isFollowing = data.followers.isFollowed,
            campaign = data.campaign,
            products = data.products,
            totalProducts = data.totalProducts,
            trackerData = trackerDataModel,
            positionInFeed = absoluteAdapterPosition
        )

        productButtonView.bindData(
            postId = data.id,
            author = data.author,
            postType = data.typename,
            isFollowing = data.followers.isFollowed,
            campaign = data.campaign,
            hasVoucher = data.hasVoucher,
            products = data.products,
            totalProducts = data.totalProducts,
            trackerData = trackerDataModel,
            positionInFeed = absoluteAdapterPosition
        )
    }

    private fun bindAsgcTags(model: FeedCardVideoContentModel) {
        asgcTagsView.bindData(model.type, model.campaign)
    }

    private fun bindCampaignRibbon(model: FeedCardVideoContentModel) {
        campaignView.bindData(
            model.type,
            model.campaign,
            model.cta,
            model.products.firstOrNull(),
            model.products,
            model.hasVoucher,
            model.isTypeProductHighlight,
            trackerDataModel ?: trackerMapper
                .transformVideoContentToTrackerModel(model),
            model.id,
            model.author,
            model.typename,
            model.followers.isFollowed,
            absoluteAdapterPosition
        )
    }

    private fun bindComments(model: FeedCardVideoContentModel) {
        commentButtonView.bind(
            if (model.isPlayContent) model.playChannelId else model.id,
            model.isPlayContent,
            model.comments.countFmt,
            trackerDataModel,
            absoluteAdapterPosition
        )

        if (model.isTypeProductHighlight) {
            commentButtonView.hide()
        } else {
            commentButtonView.show()
        }
    }

    private fun bindVideoPlayer(element: FeedCardVideoContentModel) {
        val videoPlayer = mVideoPlayer ?: listener.getVideoPlayer(element.id)
        mVideoPlayer = videoPlayer

        videoPlayer.stop()

        videoPlayer.setVideoStateListener(object : VideoStateListener {
            override fun onInitialStateLoading() {
            }

            override fun onBuffering() {
                showLoading()
                binding.iconPlay.hide()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
                binding.iconPlay.showWithCondition(!isPlaying && mIsSelected)
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
            }
        })

        binding.playerFeedVideo.player = videoPlayer.getExoPlayer()
        binding.playerControl.player = videoPlayer.getExoPlayer()

        videoPlayer.start(
            element.videoUrl,
            false,
            playWhenReady = false
        )
    }

    private fun showLoading() {
        binding.loaderFeedVideo.show()
        if (mVideoPlayer?.getExoPlayer()?.currentPosition == 0L) {
            binding.playerFeedVideo.hide()
        }
    }

    private fun hideLoading() {
        binding.loaderFeedVideo.hide()
        binding.playerFeedVideo.show()
    }

    private fun showClearView(showDisableClearMode: Boolean = true) {
        with(binding) {
            layoutAuthorInfo.root.hide()
            tvFeedCaption.hide()
            postLikeButton.root.hide()
            commentButtonView.hide()
            menuButton.hide()
            shareButton.hide()
            overlayTop.root.hide()
            overlayBottom.root.hide()
            overlayRight.root.hide()
            btnDisableClearMode.showWithCondition(showDisableClearMode)
        }

        productTagView.showClearView()
        productButtonView.showClearView()
    }

    private fun hideClearView() {
        with(binding) {
            layoutAuthorInfo.root.show()
            tvFeedCaption.show()
            postLikeButton.root.show()
            commentButtonView.show()
            menuButton.show()
            shareButton.show()
            productTagButton.root.show()
            productTagView.root.show()
            overlayTop.root.show()
            overlayBottom.root.show()
            overlayRight.root.show()
            btnDisableClearMode.hide()
        }

        productTagView.showIfPossible()
        productButtonView.showIfPossible()
    }

    private fun onScrolling(isScrolling: Boolean) {
        val startAlpha = opacityViewList.first().alpha
        if (isScrolling) {
            alphaAnimator.animateToAlpha(startAlpha)
        } else {
            alphaAnimator.animateToOpaque(startAlpha)
        }
    }

    private fun onSelected(element: FeedCardVideoContentModel) {
        mIsSelected = true
        val trackerModel =
            trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(element)
        listener.onPostImpression(
            trackerModel,
            element.id,
            absoluteAdapterPosition
        )
        campaignView.resetView()
        campaignView.startAnimation()
        mVideoPlayer?.resume()
        listener.onWatchPostVideo(element, trackerModel)
        onScrolling(false)
    }

    private fun onNotSelected() {
        mIsSelected = false
        mVideoPlayer?.pause()
        mVideoPlayer?.reset()
        onScrolling(false)

        campaignView.resetView()
        hideClearView()
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        val thePlayer = mVideoPlayer
        mVideoPlayer = null

        binding.playerFeedVideo.player = null
        binding.playerControl.player = null
        thePlayer?.let { listener.detachPlayer(it) }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_video
    }
}
