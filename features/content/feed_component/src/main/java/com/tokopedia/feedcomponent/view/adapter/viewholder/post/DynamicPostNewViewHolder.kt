package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.annotation.LayoutRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.PAYLOAD_PLAY_VIDEO
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.user.session.UserSessionInterface

private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"
private const val BROADCAST_FEED = "BROADCAST_FEED"

open class DynamicPostNewViewHolder(
    itemView: View, private val userSession: UserSessionInterface,
    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
    private val videoListener: VideoViewHolder.VideoViewListener,
    private val gridItemListener: GridPostAdapter.GridItemListener,
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
) : AbstractViewHolder<DynamicPostUiModel>(itemView) {

    private lateinit var receiver: BroadcastReceiver

    private val postDynamicView =
        itemView.findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)

    companion object {
        const val PAYLOAD_ANIMATE_LIKE = 47
        const val PAYLOAD_ANIMATE_FOLLOW = 7
        const val PAYLOAD_COMMENT = 99
        const val PAYLOAD_FRAGMENT_VISIBLE = 44
        const val PAYLOAD_FRAGMENT_GONE = 42


        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post_new
    }

    override fun bind(element: DynamicPostUiModel?, payloads: MutableList<Any>) {
        if (element == null) {
            itemView.hide()
            return
        }
        when (payloads.firstOrNull() as Int) {
            PAYLOAD_ANIMATE_LIKE -> postDynamicView.bindLike(element.feedXCard)
            PAYLOAD_ANIMATE_FOLLOW -> postDynamicView.bindFollow(element.feedXCard)
            PAYLOAD_PLAY_VIDEO -> postDynamicView.playVideo(element.feedXCard)
            PAYLOAD_COMMENT -> postDynamicView.setCommentCount(element.feedXCard.comments)
            PAYLOAD_FRAGMENT_VISIBLE -> postDynamicView.setVideo(true)
            PAYLOAD_FRAGMENT_GONE -> postDynamicView.setVideo(false)
        }
    }

    override fun bind(element: DynamicPostUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        postDynamicView.bindData(
            dynamicPostListener,
            gridItemListener,
            videoListener,
            adapterPosition,
            userSession,
            element.feedXCard,
            imagePostListener
        )
        postDynamicView.setMargin(
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12)
        )

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BROADCAST_VISIBLITY)
                    postDynamicView?.setVideo(false)
                else if (intent?.action == BROADCAST_FEED) {
                    postDynamicView?.setVideo(true)
                }
            }
        }
    }

    fun onItemDetach(context: Context?, visitable: Visitable<*>) {
        if (context?.applicationContext != null) {
            LocalBroadcastManager
                .getInstance(context.applicationContext)
                .unregisterReceiver(receiver)
        }
        postDynamicView?.detach(false, visitable as DynamicPostUiModel)
    }

    fun onItemAttach(context: Context?) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_VISIBLITY)
        intentFilter.addAction(BROADCAST_FEED)
        if (context?.applicationContext != null) {
            LocalBroadcastManager
                .getInstance(context.applicationContext)
                .registerReceiver(receiver,intentFilter)
        }
    }
}