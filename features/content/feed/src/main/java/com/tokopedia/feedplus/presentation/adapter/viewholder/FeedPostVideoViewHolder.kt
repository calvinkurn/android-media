package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
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

    private var videoPlayer: FeedExoPlayer? = null
    private var isPaused: Boolean = false

    private var feedVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            with(binding) {
                bindVideoPlayer(element)

                val authorView = FeedAuthorInfoView(layoutAuthorInfo, listener)
                authorView.bindData(data.author, false, !element.followers.isFollowed)

                val captionView = FeedCaptionView(tvFeedCaption)
                captionView.bind(element.text)

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

    private fun bindVideoPlayer(element: FeedCardVideoContentModel?) {
        if (videoPlayer == null) {
            videoPlayer = FeedExoPlayer(binding.root.context)
        }
        feedVideoJob?.cancel()
        with(binding) {
            feedVideoJob = scope.launch {
                playerFeedVideo.player = videoPlayer?.getExoPlayer()
                playerFeedVideo.videoSurfaceView?.setOnClickListener {
                    if (playerFeedVideo.player?.isPlaying == true) {
                        videoPlayer?.pause()
                    } else {
                        videoPlayer?.resume()
                    }
                }
                element?.media?.get(0)?.let {
                    videoPlayer?.start(it.mediaUrl, false)
                }
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showLoading()
                        isPaused = false
                    }

                    override fun onVideoReadyToPlay() {
                        hideLoading()
                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
//                    TODO("Not yet implemented")
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
