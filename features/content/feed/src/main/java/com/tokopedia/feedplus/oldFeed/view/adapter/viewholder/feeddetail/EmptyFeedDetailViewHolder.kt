package com.tokopedia.feedplus.oldFeed.view.adapter.viewholder.feeddetail

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.oldFeed.view.listener.FeedPlusDetailListener

/**
 * @author by nisie on 6/22/17.
 */
class EmptyFeedDetailViewHolder(itemView: View, viewListener: FeedPlusDetailListener) : AbstractViewHolder<EmptyModel>(itemView) {
    private var buttonReturn: TextView = itemView.findViewById<View>(R.id.button_return) as TextView
    override fun bind(element: EmptyModel) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_feed_detail_empty
    }

    init {
        buttonReturn.setOnClickListener { viewListener.onBackPressed() }
    }
}
