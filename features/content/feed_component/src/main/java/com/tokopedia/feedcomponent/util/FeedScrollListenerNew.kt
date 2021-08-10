package com.tokopedia.feedcomponent.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder.Companion.PAYLOAD_POST_VISIBLE
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.*


object FeedScrollListenerNew {
    private const val THRESHOLD_VIDEO_HEIGHT_SHOWN = 90
    private const val TYPE_VIDEO = "video"
    fun onFeedScrolled(recyclerView: RecyclerView, list: List<Visitable<*>>) {
        if (canAutoplayVideo(recyclerView)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
            for (i in firstPosition..lastPosition) {
                val item = getCardViewModel(list, i)
                if (isVideoCard(list, i)) {
                    if (item != null) {
                        getVideoModelScrollListener(layoutManager, recyclerView, i, item)
                    }
                } else if (isImageCard(list, i)) {
                    if (item != null) {
                        getImagePostScrollListener(layoutManager, recyclerView, i)
                    }
                }
            }
        }
    }

    private fun getImagePostScrollListener(
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int
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
            val percentVideo: Int
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            percentVideo = visibleVideo * 100 / imageView.height

            val isStateChanged: Boolean = percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN

            if (isStateChanged) {
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, PAYLOAD_POST_VISIBLE)
            }
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
            percentVideo = visibleVideo * 100 / imageView.height

            var isStateChanged = false
            if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                if (!item.canPlay) isStateChanged = true
                item.canPlay = true
            } else {
                //  if (item.canPlay) isStateChanged = true
                item.canPlay = false
            }

            if (isStateChanged) {
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO)
            }
        }
    }

    private fun isVideoCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && ((list[position] as DynamicPostUiModel).feedXCard.media.find {
            it.type == TYPE_VIDEO
        } != null))
    }


    private fun isImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel && (list[position] as DynamicPostUiModel).feedXCard.type == TYPE_FEED_X_CARD_POST
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && ((list[position] as DynamicPostUiModel).feedXCard.media.find {
            it.type == TYPE_IMAGE
        } != null))
    }

    private fun getCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            return (list[position] as DynamicPostUiModel).feedXCard.media.firstOrNull()
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }

    private fun canAutoplayVideo(recyclerView: RecyclerView): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(recyclerView.context)
        return config.getBoolean(RemoteConfigKey.CONFIG_AUTOPLAY_VIDEO_WIFI, false)
    }
}