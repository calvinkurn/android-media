package com.tokopedia.feedcomponent.view.adapter.viewholder.relatedpost

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by milhamj on 2019-08-12.
 */
class RelatedPostViewHolder(v: View,
                            private val listener: RelatedPostListener): AbstractViewHolder<RelatedPostViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_recommendation
    }

    override fun bind(element: RelatedPostViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }


    }

    interface RelatedPostListener{
        fun onClick()
    }
}