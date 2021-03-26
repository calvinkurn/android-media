package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide

class DynamicPostNewViewHolder(itemView: View) : AbstractViewHolder<DynamicPostUiModel>(itemView) {

    private val postDynamicView = itemView.findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post_new
    }

    override fun bind(element: DynamicPostUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        postDynamicView.bindHeader(element.feedXCard.author)
        postDynamicView.bindLike(element.feedXCard.like)
        postDynamicView.bindComment(element.feedXCard.comments)

    }

}