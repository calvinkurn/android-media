package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.user.session.UserSession

class DynamicPostNewViewHolder(itemView: View) : AbstractViewHolder<DynamicPostUiModel>(itemView) {

    private val postDynamicView = itemView.findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)
    private val userSession: UserSession by lazy {
        UserSession(itemView.context)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post_new
    }

    override fun bind(element: DynamicPostUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        postDynamicView.bindHeader(element.feedXCard.author, element.feedXCard.followers.isFollowed)
        postDynamicView.bindItems(element.feedXCard.media)
        postDynamicView.bindCaption(element.feedXCard)
        postDynamicView.bindPublishedAt(element.feedXCard.publishedAt, element.feedXCard.subTitle)
        postDynamicView.bindLike(element.feedXCard.like)
        postDynamicView.bindComment(element.feedXCard.comments, userSession.profilePicture, userSession.name)
        postDynamicView.setMargin(itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
                itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12),
                itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
                itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12))

    }

}