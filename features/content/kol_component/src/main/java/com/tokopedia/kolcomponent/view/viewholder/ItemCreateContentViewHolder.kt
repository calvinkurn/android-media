package com.tokopedia.kolcomponent.view.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.viewmodel.ItemCreateContentViewModel
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_feed_create_content.view.*

/**
 * @author by yfsx on 04/12/18.
 */
class ItemCreateContentViewHolder (v: View) : AbstractViewHolder<ItemCreateContentViewModel> (v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_create_content
    }

    override fun bind(element: ItemCreateContentViewModel) {
        initView(element)
        initViewListener(element)
    }

    fun initView(element: ItemCreateContentViewModel) {
        itemView.ivImage.loadImageCircle(element.profileUrl)
        itemView.tvTitle.text = element.title
        itemView.tvDescription.text = element.desc
        itemView.btnSeeProfile.text = element.btnSeeProfileText
        itemView.btnSeeProfile.buttonCompatType = ButtonCompat.SECONDARY
        itemView.btnCreateContent.text = element.btnCreateContentText
        itemView.btnCreateContent.buttonCompatType = ButtonCompat.PRIMARY
    }

    fun initViewListener(element: ItemCreateContentViewModel) {

    }
}