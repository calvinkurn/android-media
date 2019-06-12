package com.tokopedia.feedcomponent.util

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.VideoView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel

/**
 * @author by yfsx on 12/06/19.
 */
object FeedScrollListener {

    private val THRESHOLD_VIDEO_HEIGHT_SHOWN = 75

    fun onFeedScrolled(recyclerView: RecyclerView, adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = layoutManager.findFirstVisibleItemPosition();
        val lastPosition = layoutManager.findLastVisibleItemPosition();

        val rvRect = Rect();
        recyclerView.getGlobalVisibleRect(rvRect);

        for (i in firstPosition..lastPosition) {
            if (isVideoCard(adapter, i)) {
                val item = getVideoCardViewModel(adapter, i)
                val rowRect = Rect();
                layoutManager.findViewByPosition(i).getGlobalVisibleRect(rowRect);
                val videoViewRect = Rect()
                layoutManager.findViewByPosition(i).findViewById<View>(R.id.image)?.getGlobalVisibleRect(videoViewRect)
                layoutManager.findViewByPosition(i).findViewById<VideoView>(R.id.image)
                var percentVideo = 0
                if (rowRect.bottom >= rvRect.bottom) {
                    layoutManager.findViewByPosition(i).findViewById<View>(R.id.image)?.let {
                        val visibleVideo = rvRect.bottom - videoViewRect.top
                        percentVideo = (visibleVideo * 100) / it.height
                    }
                } else {
                    layoutManager.findViewByPosition(i).findViewById<View>(R.id.image)?.let {
                        val visibleVideo = videoViewRect.bottom - rvRect.top
                        percentVideo = (visibleVideo * 100) / it.height
                    }
                }
                var isStateChanged = false;
                if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                    if (!item.canPlayVideo) isStateChanged = true
                    item.canPlayVideo = true
                } else {
                    if (item.canPlayVideo) isStateChanged = true
                    item.canPlayVideo = false
                }
                if (isStateChanged)
                    recyclerView.adapter.notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO)
            }
        }
    }

    private fun isVideoCard(adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>, position: Int): Boolean {
        return adapter.list[position] is DynamicPostViewModel
                && ((adapter.list[position]) as DynamicPostViewModel).contentList.size == 1
                && ((adapter.list[position]) as DynamicPostViewModel).contentList.get(0) is VideoViewModel
    }

    private fun getVideoCardViewModel(adapter:  BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>, position: Int): VideoViewModel {
        return ((adapter.list[position]) as DynamicPostViewModel).contentList.get(0) as VideoViewModel
    }

}