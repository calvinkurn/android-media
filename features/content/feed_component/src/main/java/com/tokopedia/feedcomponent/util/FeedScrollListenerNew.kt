package com.tokopedia.feedcomponent.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.*


object FeedScrollListenerNew {
    private const val THRESHOLD_VIDEO_HEIGHT_SHOWN = 75
    private const val TYPE_VIDEO = "video"
    fun onFeedScrolled(recyclerView: RecyclerView, list: List<Visitable<*>>) {
        if (canAutoplayVideo(recyclerView)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val firstPosition = layoutManager!!.findFirstVisibleItemPosition()
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            for (i in firstPosition..lastPosition) {
                if (isVideoCard(list, i)) {
                    val item = getVideoCardViewModel(list, i)
                    if (item != null) {
                        getVideoModelScrollListener(item, layoutManager, recyclerView, i)
                    }
                }
            }
        }
    }

    private fun getVideoModelScrollListener(
        item: FeedXMedia,
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager!!.findViewByPosition(i)!!.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager.findViewByPosition(i)!!.findViewById<View>(R.id.videoPreviewImage)
            .getGlobalVisibleRect(videoViewRect)
        val imageView =
            layoutManager.findViewByPosition(i)!!.findViewById<View>(R.id.videoPreviewImage)
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
                if (!item.canPlayVideo) isStateChanged = true
                item.canPlayVideo = true
            } else {
                if (item.canPlayVideo) isStateChanged = true
                item.canPlayVideo = false
            }
            if (isStateChanged) {
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO)
            }
        }
    }


    private fun isVideoCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is DynamicPostUiModel
                && (list[position] as DynamicPostUiModel).feedXCard.media.isNotEmpty() && (list[position] as DynamicPostUiModel).feedXCard.media[0].type.equals(
            TYPE_VIDEO,
            ignoreCase = true
        ))
    }


    private fun getVideoCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            return (list[position] as DynamicPostUiModel).feedXCard.media[0]
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