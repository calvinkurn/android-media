package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroBenefitImageItem
import kotlinx.android.synthetic.main.tm_dash_intro_benefit_image_item.view.*

class TokomemberIntroBenefitImageVh(val view: View)
    : AbstractViewHolder<TokomemberIntroBenefitImageItem>(view) {

    private val tmIntroVideoView = itemView.ivSection

    override fun bind(element: TokomemberIntroBenefitImageItem?) {
        element?.apply {
            tmIntroVideoView?.loadImage(element.imgUrl)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_benefit_image_item
    }
}
