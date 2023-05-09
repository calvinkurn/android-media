package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.customview.FeedPlayerControl
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedAsgcTagsView
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.feedplus.presentation.uiview.FeedProductButtonView
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.animation.FeedLikeAnimationComponent
import com.tokopedia.feedplus.presentation.util.animation.FeedSmallLikeIconAnimationComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
class FeedPostVideoViewHolder(
    private val binding: ItemFeedPostVideoBinding,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardVideoContentModel>(binding.root) {

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption, listener)
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView = FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(binding.root)
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)

    private var mVideoPlayer: FeedExoPlayer? = null
    private var mData: FeedCardVideoContentModel? = null
    private var trackerDataModel: FeedTrackerDataModel? = null

    init {
        binding.playerControl.setListener(object : FeedPlayerControl.Listener {
            override fun onScrubbing(
                view: PlayerControlView, currPosition: Long, totalDuration: Long
            ) {
                binding.videoTimeView.setCurrentPosition(currPosition)
                binding.videoTimeView.setTotalDuration(totalDuration)
                binding.videoTimeView.show()
                showClearView(showDisableClearMode = false)
            }

            override fun onStopScrubbing(
                view: PlayerControlView, currPosition: Long, totalDuration: Long
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
    }

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            mData = data
            trackerDataModel = trackerMapper.transformVideoContentToTrackerModel(data)

            with(binding) {
                GestureDetector(root.context,
                    object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                            return true
                        }

                        override fun onDoubleTap(e: MotionEvent): Boolean {
                            if (data.like.isLiked.not()) {
                                listener.onLikePostCLicked(
                                    data.id,
                                    data.like.isLiked,
                                    absoluteAdapterPosition,
                                    trackerDataModel
                                        ?: trackerMapper.transformVideoContentToTrackerModel(data),
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
                    })

                bindAuthor(data)
                bindCaption(data)
                bindProductTag(data)
                bindLike(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)
                bindComments(data)
                bindVideoPlayer(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(
                        data.id,
                        trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                            data
                        )
                    )
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(
                        id = data.id,
                        authorName = data.author.name,
                        applink = data.applink,
                        weblink = data.weblink,
                        imageUrl = data.media.firstOrNull()?.coverUrl ?: ""
                    )
                }
                postLikeButton.likeButton.setOnClickListener {
                    listener.onLikePostCLicked(
                        data.id,
                        data.like.isLiked,
                        absoluteAdapterPosition,
                        trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                            data
                        ),
                        false
                    )
                }
                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }
            }
        }
    }

    override fun bind(element: FeedCardVideoContentModel?, payloads: MutableList<Any>) {
        element?.let {
            mData = it
            trackerDataModel = trackerMapper.transformVideoContentToTrackerModel(it)

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(it.like)
            }
            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE)) {
                showClearView()
            }
            if (payloads.contains(FEED_POST_SELECTED)) {
                listener.onPostImpression(
                    trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                        it
                    ),
                    it.id,
                    absoluteAdapterPosition
                )
                campaignView.startAnimation()
                mVideoPlayer?.resume()
                listener.onWatchPostVideo(
                    trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                        it
                    )
                )
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                mVideoPlayer?.pause()
                mVideoPlayer?.reset()

                campaignView.resetView()
                hideClearView()
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
        likeAnimationView.setIsLiked(like.isLiked)
    }

    private fun bindProductTag(data: FeedCardVideoContentModel) {
        productTagView.bindData(
            postId = data.id,
            author = data.author,
            postType = data.typename,
            isFollowing = data.followers.isFollowed,
            campaign = data.campaign,
            hasVoucher = data.hasVoucher,
            products = data.products,
            totalProducts = data.totalProducts,
            trackerData = trackerDataModel
        )

        productButtonView.bindData(
            postId = data.id,
            author = data.author,
            postType = data.typename,
            isFollowing = data.followers.isFollowed,
            campaign = data.campaign,
            hasVoucher = data.hasVoucher,
            products = data.products,
            trackerData = trackerDataModel
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
            model.hasVoucher,
            model.isTypeProductHighlight,
            trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                model
            )
        )
    }

    private fun bindComments(model: FeedCardVideoContentModel) {
        if (model.isTypeProductHighlight) {
            binding.commentButton.hide()
        } else {
            binding.commentButton.show()
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
                binding.iconPlay.showWithCondition(!isPlaying)
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
            }
        })

        binding.playerFeedVideo.player = videoPlayer.getExoPlayer()
        binding.playerControl.player = videoPlayer.getExoPlayer()
        binding.playerFeedVideo.videoSurfaceView?.setOnClickListener {
            videoPlayer.getExoPlayer().playWhenReady = !videoPlayer.getExoPlayer().playWhenReady

            if (!videoPlayer.getExoPlayer().playWhenReady) {
                listener.onPauseVideoPost(
                    trackerDataModel ?: trackerMapper.transformVideoContentToTrackerModel(
                        element
                    )
                )
            }
        }

        videoPlayer.start(
            element.media.firstOrNull()?.mediaUrl.orEmpty(),
            false,
            playWhenReady = false,
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
            commentButton.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.root.hide()
            productTagView.root.hide()
            overlayTop.root.hide()
            overlayBottom.root.hide()
            overlayRight.root.hide()
            btnDisableClearMode.showWithCondition(showDisableClearMode)
        }
    }

    private fun hideClearView() {
        with(binding) {
            layoutAuthorInfo.root.show()
            tvFeedCaption.show()
            postLikeButton.root.show()
            commentButton.show()
            menuButton.show()
            shareButton.show()
            productTagButton.root.show()
            productTagView.root.show()
            overlayTop.root.show()
            overlayBottom.root.show()
            overlayRight.root.show()
            btnDisableClearMode.hide()
        }
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
