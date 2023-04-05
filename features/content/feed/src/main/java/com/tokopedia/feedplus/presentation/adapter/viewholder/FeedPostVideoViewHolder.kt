package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
class FeedPostVideoViewHolder(
    private val binding: ItemFeedPostVideoBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardVideoContentModel>(binding.root) {

    private var feedVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private val videoPlayer = listener.getVideoPlayer()

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption)
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView = FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(binding.root)
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            with(binding) {
                bindAuthor(data)
                bindCaption(data)
                bindProductTag(data)
                bindLike(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(data.id)
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
                    listener.onLikePostCLicked(data.id, data.like.isLiked, absoluteAdapterPosition)
                }
                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }
            }
        }
    }

    override fun bind(element: FeedCardVideoContentModel?, payloads: MutableList<Any>) {
        element?.let {
            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(element.like)
            }
            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE)) {
                showClearView()
            }
            if (payloads.contains(FEED_POST_SELECTED)) {
                campaignView.startAnimation()
                bindVideoPlayer(element)
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                videoPlayer.stop()
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
        authorView.bindData(data.author, false, !data.followers.isFollowed)
    }

    private fun bindCaption(data: FeedCardVideoContentModel) {
        captionView.bind(data.text)
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
            totalProducts = data.totalProducts
        )

        productButtonView.bindData(
            postId = data.id,
            author = data.author,
            postType = data.typename,
            isFollowing = data.followers.isFollowed,
            campaign = data.campaign,
            hasVoucher = data.hasVoucher,
            totalProducts = data.totalProducts
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
            model.isTypeProductHighlight
        )
    }

    private fun bindVideoPlayer(element: FeedCardVideoContentModel) {
        feedVideoJob?.cancel()
        with(binding) {
            feedVideoJob = scope.launch {
                playerFeedVideo.player = videoPlayer.getExoPlayer()
                playerControl.player = videoPlayer.getExoPlayer()
                playerFeedVideo.videoSurfaceView?.setOnClickListener {
                    videoPlayer.getExoPlayer().playWhenReady =
                        !videoPlayer.getExoPlayer().playWhenReady
                }
                element.media[0].let {
                    videoPlayer.start(it.mediaUrl, false)
                }
                videoPlayer.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
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
            }
        }
    }

    private fun showLoading() {
        binding.loaderFeedVideo.show()
        binding.playerFeedVideo.hide()
    }

    private fun hideLoading() {
        binding.loaderFeedVideo.hide()
        binding.playerFeedVideo.show()
    }

    private fun showClearView() {
        with(binding) {
            layoutAuthorInfo.root.hide()
            tvFeedCaption.hide()
            postLikeButton.root.hide()
            commentButton.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.root.hide()
            productTagView.root.hide()
            btnDisableClearMode.show()
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
            btnDisableClearMode.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_video
    }
}
