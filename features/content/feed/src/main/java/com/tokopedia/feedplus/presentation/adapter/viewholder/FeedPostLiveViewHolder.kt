package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.ContentItemComponentsAlphaAnimator
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
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.util.extension.changeConstraint
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding

/**
 * Created By : Muhammad Furqan on 09/03/23
 */
class FeedPostLiveViewHolder(
    private val binding: ItemFeedPostLiveBinding,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardLivePreviewContentModel>(binding.root) {

    private val alphaAnimator = ContentItemComponentsAlphaAnimator(object : ContentItemComponentsAlphaAnimator.Listener {
        override fun onAnimateAlpha(animator: ContentItemComponentsAlphaAnimator, alpha: Float) {
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

        binding.layoutFeedCaption.doOnApplyWindowInsets { view, insets, padding, _ ->
            val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                bottom = padding.bottom + systemBarInset.bottom
            )
        }
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

    private fun handleResumeLiveVideo(element: FeedCardLivePreviewContentModel) {
        if (element.isLive && listener.isAllowedToPlayVideo()) {
            mVideoPlayer?.resume(shouldReset = false)
            hideLiveEndView()
        } else if (!element.isLive) {
            showLiveEndView()
        }
    }

    private fun onSelected(element: FeedCardLivePreviewContentModel) {
        listener.checkLiveStatus(element.playChannelId)
        listener.onPostImpression(
            trackerDataModel ?: trackerMapper.transformLiveContentToTrackerModel(
                element
            ),
            element.id,
            absoluteAdapterPosition
        )

        mVideoPlayer?.toggleVideoVolume(listener.isMuted())
        handleResumeLiveVideo(element)
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
        authorView.bindData(
            data.author,
            data.isLive,
            false,
            trackerDataModel,
            null
        )
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
                if (element.isLive) showLoading()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
            }

            override fun onBehindLiveWindow(playWhenReady: Boolean) {
                startVideo(
                    videoPlayer = videoPlayer,
                    videoUrl = element.videoUrl,
                    isMute = false,
                    playWhenReady = playWhenReady,
                    isLiveContent = true,
                    stillLive = element.isLive
                )
            }
        })

        binding.playerFeedVideo.player = videoPlayer.getExoPlayer()
        startVideo(
            videoPlayer = videoPlayer,
            videoUrl = element.videoUrl,
            isMute = false,
            playWhenReady = false,
            isLiveContent = true,
            stillLive = element.isLive
        )
    }

    private fun startVideo(
        videoPlayer: FeedExoPlayer,
        videoUrl: String,
        isMute: Boolean,
        playWhenReady: Boolean,
        isLiveContent: Boolean,
        stillLive: Boolean
    ) {
        if (stillLive) {
            videoPlayer.start(
                videoUrl,
                isMute = isMute,
                playWhenReady = playWhenReady,
                isLive = isLiveContent
            )
        }
    }

    private fun showLoading() {
        binding.loaderFeedVideo.show()
        binding.containerFeedLiveEnd.root.hide()
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

    private fun showLiveEndView() {
        with(binding) {
            containerFeedLiveEnd.root.show()
            playerFeedVideo.hide()
            feedLiveWaveLabel.hide()
            loaderFeedVideo.hide()
        }
    }

    private fun hideLiveEndView() {
        with(binding) {
            containerFeedLiveEnd.root.hide()
            playerFeedVideo.show()
            feedLiveWaveLabel.show()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post_live
    }
}
