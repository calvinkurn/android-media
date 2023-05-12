package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostLiveBinding
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
class FeedPostLiveViewHolder(
    private val binding: ItemFeedPostLiveBinding,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardLivePreviewContentModel>(binding.root) {

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption, listener)

    private var trackerDataModel: FeedTrackerDataModel? = null

    private var mVideoPlayer: FeedExoPlayer? = null

    override fun bind(element: FeedCardLivePreviewContentModel?) {
        element?.let { data ->
            trackerDataModel = trackerMapper.transformLiveContentToTrackerModel(data)

            bindAuthor(data)
            bindCaption(data)
            bindVideoPlayer(data)

            binding.root.setOnClickListener {
                listener.onLivePreviewClicked(
                    trackerDataModel,
                    absoluteAdapterPosition,
                    data.products.firstOrNull()?.id.orEmpty(),
                    data.author.name
                )
                RouteManager.route(binding.root.context, data.applink)
            }
        }
    }

    override fun bind(element: FeedCardLivePreviewContentModel?, payloads: MutableList<Any>) {
        element?.let {
            trackerDataModel = trackerMapper.transformLiveContentToTrackerModel(it)

            if (payloads.contains(FEED_POST_SELECTED)) {
                listener.onPostImpression(
                    trackerDataModel ?: trackerMapper.transformLiveContentToTrackerModel(
                        it
                    ),
                    it.id,
                    absoluteAdapterPosition
                )

                mVideoPlayer?.resume(shouldReset = false)
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                mVideoPlayer?.pause()
            }
        }
    }

    override fun onViewRecycled() {
        val thePlayer = mVideoPlayer
        mVideoPlayer = null

        binding.playerFeedVideo.player = null
        thePlayer?.let { listener.detachPlayer(it) }
    }

    private fun bindAuthor(data: FeedCardLivePreviewContentModel) {
        authorView.bindData(data.author, true, !data.followers.isFollowed, trackerDataModel)
    }

    private fun bindCaption(data: FeedCardLivePreviewContentModel) {
        captionView.bind(data.text, trackerDataModel)
    }

    private fun bindVideoPlayer(element: FeedCardLivePreviewContentModel) {
        val videoPlayer = mVideoPlayer ?: listener.getVideoPlayer(element.id)
        mVideoPlayer = videoPlayer

        videoPlayer.stop()

        videoPlayer.setVideoStateListener(object : VideoStateListener {
            override fun onInitialStateLoading() {

            }

            override fun onBuffering() {
                showLoading()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
            }
        })

        binding.playerFeedVideo.player = videoPlayer.getExoPlayer()
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

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_live
    }
}
