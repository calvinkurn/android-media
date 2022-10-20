package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.PAYLOAD_PLAY_VIDEO
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.PAYLOAD_PLAY_VOD
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unifyprinciples.R as unifyR

private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"
private const val BROADCAST_FEED = "BROADCAST_FEED"

open class DynamicPostNewViewHolder(
    itemView: View, private val userSession: UserSessionInterface,
    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
    private val videoListener: VideoViewHolder.VideoViewListener,
    private val gridItemListener: GridPostAdapter.GridItemListener,
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
) : AbstractViewHolder<DynamicPostUiModel>(itemView) {

    private val postDynamicView =
        itemView.findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)

    companion object {
        const val PAYLOAD_ANIMATE_LIKE = 47
        const val PAYLOAD_ANIMATE_FOLLOW = 7
        const val PAYLOAD_COMMENT = 99
        const val PAYLOAD_FRAGMENT_VISIBLE = 44
        const val PAYLOAD_FRAGMENT_GONE = 42
        const val PAYLOAD_POST_VISIBLE = 77
        const val PAYLOAD_CTA_VISIBLE = 88
        const val PAYLOAD_REMINDER_BTN_STATUS_UPDATED = 56


        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post_new
    }

    override fun bind(element: DynamicPostUiModel?, payloads: MutableList<Any>) {
        bind(element, payloads, "")
    }

    fun bind(element: DynamicPostUiModel?, payloads: MutableList<Any>,
                      broadcastValueForDynamicPost: String) {
        if (element == null) {
            itemView.hide()
            return
        }
        when (payloads.firstOrNull() as Int) {
            PAYLOAD_ANIMATE_LIKE -> postDynamicView.bindLike(element.feedXCard)
            PAYLOAD_ANIMATE_FOLLOW -> postDynamicView.bindFollow(element.feedXCard)
            PAYLOAD_PLAY_VIDEO -> postDynamicView.playVideo(element.feedXCard)
            PAYLOAD_PLAY_VOD -> postDynamicView.playVOD(element.feedXCard)
            PAYLOAD_COMMENT -> postDynamicView.setCommentCount(element.feedXCard.comments)
            PAYLOAD_FRAGMENT_VISIBLE -> {
                if (broadcastValueForDynamicPost.isEmpty()) {
                    postDynamicView.setVideo(true)
                } else {
                    setPostDynamicView(broadcastValueForDynamicPost)
                }
            }
            PAYLOAD_FRAGMENT_GONE -> {
                if (broadcastValueForDynamicPost.isEmpty()) {
                    postDynamicView.setVideo(false)
                } else {
                    setPostDynamicView(broadcastValueForDynamicPost)
                }
            }
            PAYLOAD_POST_VISIBLE -> postDynamicView.bindImage(
                element.feedXCard.tags,
                element.feedXCard.media[element.feedXCard.lastCarouselIndex],
                element.feedXCard
            )
            PAYLOAD_CTA_VISIBLE -> postDynamicView.onCTAVisible(element.feedXCard)
            PAYLOAD_REMINDER_BTN_STATUS_UPDATED -> postDynamicView.onFSTReminderStatusUpdated()
        }
    }

    fun setPostDynamicView(visibility:String) {
        if (visibility == BROADCAST_VISIBLITY) {
            postDynamicView?.setVideo(false)
        } else {
            postDynamicView?.setVideo(true)
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
            itemView.context.resources.getDimensionPixelSize(unifyR.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(unifyR.dimen.unify_space_12),
            itemView.context.resources.getDimensionPixelSize(unifyR.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(unifyR.dimen.unify_space_12)
        )
    }

    fun bind(element: DynamicPostUiModel?, broadcastValueForDynamicPost: String) {
        bind(element)
        setPostDynamicView(broadcastValueForDynamicPost)
    }

    fun onItemDetach(context: Context?, visitable: Visitable<*>) {
        try {
            postDynamicView?.detach(false, visitable as DynamicPostUiModel)
        } catch (e: Exception) {
        }
    }

    fun onItemAttach(context: Context?, visitable: Visitable<*>) {
        try {
            postDynamicView?.attach( visitable as DynamicPostUiModel)
        } catch (e: Exception) {
        }
    }
}