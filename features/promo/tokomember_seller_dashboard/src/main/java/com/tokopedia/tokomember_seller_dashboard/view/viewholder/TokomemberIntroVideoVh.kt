package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroVideoItem
import kotlinx.android.synthetic.main.tm_dash_intro_header.view.*
import kotlinx.android.synthetic.main.tm_dash_video_view_item.view.*

class TokomemberIntroVideoVh(val view: View)
    : AbstractViewHolder<TokomemberIntroVideoItem>(view) {

    private val tmIntroVideoView = itemView.tmVideoView

    override fun bind(element: TokomemberIntroVideoItem?) {
        element?.apply {
            tmIntroVideoView?.playVideo(element.url?:"")
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_video_view_item
    }
}
