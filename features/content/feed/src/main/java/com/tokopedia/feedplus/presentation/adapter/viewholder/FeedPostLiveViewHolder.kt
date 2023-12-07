package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostLiveBinding
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_DONE_SCROLL
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SCROLLING
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SCROLLING_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloads
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.feedplus.presentation.util.animation.FeedPostAlphaAnimator
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.util.extension.changeConstraint

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
class FeedPostLiveViewHolder(
    private val binding: ItemFeedPostLiveBinding,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardLivePreviewContentModel>(binding.root) {

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

    private var trackerDataModel: FeedTrackerDataModel? = null

    private var mVideoPlayer: FeedExoPlayer? = null

    private val opacityViewList = listOf(
        binding.layoutAuthorInfo.root,
        binding.tvFeedCaption,
        binding.overlayTop.root,
        binding.overlayBottom.root,
        binding.feedLiveWaveLabel
    )

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {}

            override fun onViewDetachedFromWindow(p0: View) {
                onNotSelected()
            }
        })
    }

    fun bind(item: FeedContentAdapter.Item) {
        val data = item.data as FeedCardLivePreviewContentModel
        bind(data)

        if (item.isSelected) {
            onSelected(data)
        } else {
            onNotSelected()
        }
    }

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

    fun bind(item: FeedContentAdapter.Item, payloads: MutableList<Any>) {
        val selectedPayload = if (item.isSelected) FEED_POST_SELECTED else FEED_POST_NOT_SELECTED
        val scrollingPayload = if (item.isScrolling) FEED_POST_SCROLLING else FEED_POST_DONE_SCROLL

        val feedPayloads =
            payloads.firstOrNull { it is FeedViewHolderPayloads } as? FeedViewHolderPayloads

        if (feedPayloads == null) {
            bind(item.data as FeedCardLivePreviewContentModel, payloads)
        } else {
            val newPayloads = mutableListOf<Any>().apply {
                addAll(payloads)
                if (feedPayloads.payloads.contains(FEED_POST_SELECTED_CHANGED)) add(selectedPayload)
                if (feedPayloads.payloads.contains(FEED_POST_SCROLLING_CHANGED)) {
                    add(
                        scrollingPayload
                    )
                }
            }
            bind(item.data as FeedCardLivePreviewContentModel, newPayloads)
        }
    }

    override fun bind(element: FeedCardLivePreviewContentModel?, payloads: MutableList<Any>) {
        element?.let {
            trackerDataModel = trackerMapper.transformLiveContentToTrackerModel(it)

            if (payloads.contains(FEED_POST_SELECTED)) {
                onSelected(element)
            }

            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                onNotSelected()
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

    private fun onSelected(element: FeedCardLivePreviewContentModel) {
        listener.onPostImpression(
            trackerDataModel ?: trackerMapper.transformLiveContentToTrackerModel(
                element
            ),
            element.id,
            absoluteAdapterPosition
        )

        mVideoPlayer?.toggleVideoVolume(listener.isMuted())
        if (listener.isAllowedToPlayVideo()) mVideoPlayer?.resume(shouldReset = false)
        onScrolling(false)
    }

    private fun onNotSelected() {
        mVideoPlayer?.pause()
        onScrolling(false)
    }

    override fun onViewRecycled() {
        val thePlayer = mVideoPlayer
        mVideoPlayer = null

        binding.playerFeedVideo.player = null
        thePlayer?.let { listener.detachPlayer(it) }
    }

    private fun bindAuthor(data: FeedCardLivePreviewContentModel) {
        authorView.bindData(data.author, true, !data.followers.isFollowed, trackerDataModel, null)
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

            override fun onBehindLiveWindow(playWhenReady: Boolean) {
                videoPlayer.start(
                    element.videoUrl,
                    false,
                    playWhenReady = playWhenReady,
                    isLive = true
                )
            }
        })

        binding.playerFeedVideo.player = videoPlayer.getExoPlayer()
        videoPlayer.start(
            element.videoUrl,
            false,
            playWhenReady = false,
            isLive = true
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

    private fun onScrolling(isScrolling: Boolean) {
        val startAlpha = opacityViewList.first().alpha
        if (isScrolling) {
            alphaAnimator.animateToAlpha(startAlpha)
        } else {
            alphaAnimator.animateToOpaque(startAlpha)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_live
    }
}
