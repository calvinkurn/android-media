package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.search.model.visitor.NotFoundModel
import com.tokopedia.unifyprinciples.Typography

class NotFoundViewHolder(itemView: View) : AbstractViewHolder<NotFoundModel>(itemView) {

    private val title: Typography = itemView.findViewById(R.id.tv_not_found_title)
    private val desc: Typography = itemView.findViewById(R.id.tv_not_found_desc)

    override fun bind(element: NotFoundModel?) {
        title.text = element?.title
        desc.text = element?.desc
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_search_not_found
    }
}