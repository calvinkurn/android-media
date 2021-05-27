package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.user.session.UserSessionInterface

class DynamicPostNewViewHolder(itemView: View, private val userSession: UserSessionInterface,
                               private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
                               private val videoListener: VideoViewHolder.VideoViewListener,
                               private val gridItemListener: GridPostAdapter.GridItemListener)
    : AbstractViewHolder<DynamicPostUiModel>(itemView) {

    private val postDynamicView = itemView.findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)

    companion object {
        const val PAYLOAD_ANIMATE_LIKE = 47
        const val PAYLOAD_ANIMATE_FOLLOW = 7
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post_new
    }

    override fun bind(element: DynamicPostUiModel?, payloads: MutableList<Any>) {
        if (element == null) {
            itemView.hide()
            return
        }
        when(payloads.firstOrNull() as Int){
            PAYLOAD_ANIMATE_LIKE -> postDynamicView.bindLike(element.feedXCard)
            PAYLOAD_ANIMATE_FOLLOW -> postDynamicView.bindFollow(element.feedXCard)
        }
    }

    override fun bind(element: DynamicPostUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        postDynamicView.bindData(dynamicPostListener, gridItemListener,videoListener,adapterPosition, userSession, element.feedXCard)
        postDynamicView.setMargin(itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
            itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12))
    }

}