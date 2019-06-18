package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget

class ItemBusinessViewHolder(itemView: View?) : AbstractViewHolder<HomeWidget.ContentItemTab>(itemView) {

    companion object {
        val LAYOUT: Int = R.layout.layout_template_small_business
    }

    override fun bind(element: HomeWidget.ContentItemTab?) {
        itemView
    }

}
