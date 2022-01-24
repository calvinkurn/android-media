package com.tokopedia.feedcomponent.util

import android.content.Context
import android.graphics.Rect
import android.net.wifi.WifiManager
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.*


object FeedScrollListenerNew {
    private const val THRESHOLD_VIDEO_HEIGHT_SHOWN = 90
    private const val TOTAL_VIDEO_HEIGHT_PERCENT = 100
    private const val PAYLOAD_POST_TOPADS_VISIBLE= 77
    private const val TYPE_VIDEO = "video"
    private const val TYPE_LONG_VIDEO = "long-video"
    fun  onFeedScrolled(recyclerView: RecyclerView, list: List<Visitable<*>>) {
        if (canAutoplayVideo(recyclerView)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
            for (i in firstPosition..lastPosition) {
                val item = getCardViewModel(list, i)
                val topadsItem = getTopadsCardViewModel(list,i)
                if (isVideoCard(list, i) && isWifiEnabled(recyclerView.context)) {
                    if (item != null) {
                        getVideoModelScrollListener(layoutManager, recyclerView, i, item)
                    }
                } else if (isImageCard(list, i)) {
                    if (item != null) {
                        getImagePostScrollListener(layoutManager, recyclerView, i, item)
                    }
                } else if (isVODCard(list, i) && isWifiEnabled(recyclerView.context)){
                    if (item != null) {
                        getVODModelScrollListener(layoutManager, recyclerView, i, item)
                    }
                }
                else if (isTopadsImageCard(list, i)) {
                    if (topadsItem != null) {
                        getImagePostScrollListener(layoutManager, recyclerView, i, topadsItem, true)
                    }
                }
            }
        }
    }

    private fun getImagePostScrollListener(
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int,
        item: FeedXMedia,
        isTopads:Boolean = false
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager?.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.post_image)
            ?.getGlobalVisibleRect(videoViewRect)
        val imageView =
            layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.post_image)
        if (imageView != null) {
            var percentVideo: Int = -1
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            try {
                percentVideo = visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height
            } catch (e: Exception) {
            }
            val isStateChanged: Boolean = percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN
            if (isStateChanged && item.isImageImpressedFirst) {
                    item.isImageImpressedFirst = false
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, PAYLOAD_POST_TOPADS_VISIBLE)
            }
            if(percentVideo <= 0)
                item.isImageImpressedFirst = true
        }
    }

    private fun getVideoModelScrollListener(
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int,
        item: FeedXMedia
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager?.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.videoPreviewImage)
            ?.getGlobalVisibleRect(videoViewRect)
        val imageView =
            layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.videoPreviewImage)
        if (imageView != null) {
            val percentVideo: Int
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            percentVideo = visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height

            var isStateChanged = false
            if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                if (!item.canPlay) isStateChanged = true
                item.canPlay = true
            } else {
                if(percentVideo <= 0)
                    item.isImageImpressedFirst = true
                item.canPlay = false
            }

            if (isStateChanged && item.isImageImpressedFirst) {
                item.isImageImpressedFirst = false
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO)
            }
        }
    }

    private fun getVODModelScrollListener(
            layoutManager: LinearLayoutManager?,
            recyclerView: RecyclerView,
            i: Int,
            item: FeedXMedia
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager?.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.vod_videoPreviewImage)
                ?.getGlobalVisibleRect(videoViewRect)
        val imageView =
                layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.vod_videoPreviewImage)
        if (imageView != null) {
            val percentVideo: Int
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            percentVideo = visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height

            var isStateChanged = false
            if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                if (!item.canPlay) isStateChanged = true
                item.canPlay = true
            } else {
                if(percentVideo <= 0)
                    item.isImageImpressedFirst = true
                item.canPlay = false
            }

            if (isStateChanged && item.isImageImpressedFirst) {
                item.isImageImpressedFirst = false
                Objects.requireNonNull(recyclerView.adapter)
                        .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VOD)
            }
        }
    }


    private fun isVideoCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel
                && (list[position] as DynamicPostUiModel).feedXCard.typename == TYPE_FEED_X_CARD_POST
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && ((list[position] as DynamicPostUiModel).feedXCard.media.find {
            it.type == TYPE_VIDEO || it.type == TYPE_LONG_VIDEO
        } != null))
    }
    private fun isVODCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel
                && (list[position] as DynamicPostUiModel).feedXCard.typename == TYPE_FEED_X_CARD_PLAY
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && ((list[position] as DynamicPostUiModel).feedXCard.media.find {
            it.type == TYPE_VIDEO
        } != null))
    }


    private fun isImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel &&
                ((list[position] as DynamicPostUiModel).feedXCard.typename == TYPE_FEED_X_CARD_POST || (list[position] as DynamicPostUiModel).feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT)
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && ((list[position] as DynamicPostUiModel).feedXCard.media.find {
            it.type == TYPE_IMAGE
        } != null))
    }

    private fun getCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            val feedXCard = (list[position] as DynamicPostUiModel).feedXCard
            return feedXCard.media[feedXCard.lastCarouselIndex]
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }

    private fun isTopadsImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is TopadsHeadLineV2Model && (list[position] as TopadsHeadLineV2Model).feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW
                && (list[position] as TopadsHeadLineV2Model).feedXCard.media.isNotEmpty() && ((list[position] as TopadsHeadLineV2Model).feedXCard.media.find {
            it.type == TYPE_IMAGE
        } != null))
    }

    private fun getTopadsCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            val feedXCard = (list[position] as TopadsHeadLineV2Model).feedXCard
            return feedXCard.media[feedXCard.lastCarouselIndex]
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }

    private fun canAutoplayVideo(recyclerView: RecyclerView): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(recyclerView.context)
        return config.getBoolean(RemoteConfigKey.CONFIG_AUTOPLAY_VIDEO_WIFI, false)
    }
    private fun isWifiEnabled(context: Context) : Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}