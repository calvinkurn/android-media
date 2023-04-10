package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostLiveBinding
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
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
class FeedPostLiveViewHolder(
    private val binding: ItemFeedPostLiveBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardVideoContentModel>(binding.root) {

    private var feedVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var videoPlayer: FeedExoPlayer? = null

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption)

    override fun bind(element: FeedCardVideoContentModel?) {
        element?.let { data ->
            bindAuthor(data)
            bindCaption(data)

            binding.root.setOnClickListener {
                RouteManager.route(binding.root.context, data.applink)
            }
        }
    }

    override fun bind(element: FeedCardVideoContentModel?, payloads: MutableList<Any>) {
        element?.let {
            if (payloads.contains(FEED_POST_SELECTED)) {
                bindVideoPlayer(element)
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                videoPlayer?.stop()
            }
        }
    }

    override fun onViewRecycled() {
        videoPlayer?.destroy()
    }

    private fun bindAuthor(data: FeedCardVideoContentModel) {
        authorView.bindData(data.author, false, !data.followers.isFollowed)
    }

    private fun bindCaption(data: FeedCardVideoContentModel) {
        captionView.bind(data.text)
    }

    private fun bindVideoPlayer(element: FeedCardVideoContentModel) {
        feedVideoJob?.cancel()
        with(binding) {
            if (videoPlayer == null) {
                videoPlayer = FeedExoPlayer(root.context)
            }

            feedVideoJob = scope.launch {
                playerFeedVideo.player = videoPlayer?.getExoPlayer()
                element.media[0].let {
                    videoPlayer?.start(it.mediaUrl, false)
                }
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showLoading()
                    }

                    override fun onVideoReadyToPlay(isPlaying: Boolean) {
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
        val LAYOUT = R.layout.item_feed_post_live
    }
}
