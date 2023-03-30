package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.uiview.FeedAsgcTagsView
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.feedplus.presentation.uiview.FeedProductButtonView
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            with(binding) {
                bindAuthor(element)
                bindCaption(element)
                bindProductTag(element)
                bindAsgcTags(element)

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
            }
        }
    }

    override fun bind(element: FeedCardVideoContentModel?, payloads: MutableList<Any>) {
        element?.let {
            if (payloads.contains(FEED_POST_SELECTED)) {
                bindVideoPlayer(element)
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
            }
        }
    }

    private fun bindAuthor(data: FeedCardVideoContentModel) {
        val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
        authorView.bindData(data.author, false, !data.followers.isFollowed)
    }

    private fun bindCaption(data: FeedCardVideoContentModel) {
        val captionView = FeedCaptionView(binding.tvFeedCaption)
        captionView.bind(data.text)
    }

    private fun bindProductTag(data: FeedCardVideoContentModel) {
        val productTagView = FeedProductTagView(binding.productTagView, listener)
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

        val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
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
        val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
        asgcTagsView.bindData(model.type, model.campaign)
    }

    private fun bindVideoPlayer(element: FeedCardVideoContentModel) {
        feedVideoJob?.cancel()
        with(binding) {
            feedVideoJob = scope.launch {
                playerFeedVideo.player = videoPlayer.getExoPlayer()
                playerControl.player = videoPlayer.getExoPlayer()
                playerFeedVideo.videoSurfaceView?.setOnClickListener {
                    if (playerFeedVideo.player?.isPlaying == true) {
                        videoPlayer.pause()
                    } else {
                        videoPlayer.resume()
                    }
                }
                element.media[0].let {
                    videoPlayer.start(it.mediaUrl, false)
                }
                videoPlayer.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showLoading()
                    }

                    override fun onVideoReadyToPlay() {
                        hideLoading()
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

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_video
    }
}
