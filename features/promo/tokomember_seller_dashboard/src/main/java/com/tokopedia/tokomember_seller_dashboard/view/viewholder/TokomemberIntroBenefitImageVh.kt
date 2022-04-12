package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroBenefitImageItem
import kotlinx.android.synthetic.main.tm_dash_video_view_item.view.*

class TokomemberIntroBenefitImageVh(val view: View)
    : AbstractViewHolder<TokomemberIntroBenefitImageItem>(view) {

    private val tmIntroVideoView = itemView.tmVideoView

    override fun bind(element: TokomemberIntroBenefitImageItem?) {
        element?.apply {
            tmIntroVideoView?.playVideo(element.imgUrl?:"")
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_video_view_item
    }
}
