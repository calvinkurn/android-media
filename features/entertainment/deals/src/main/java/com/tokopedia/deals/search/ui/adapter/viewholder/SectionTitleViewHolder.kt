package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.search.model.visitor.SectionTitleModel

class SectionTitleViewHolder(itemView: View): AbstractViewHolder<SectionTitleModel>(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_popular_deals_name)

    override fun bind(element: SectionTitleModel?) {
        title.text = element?.title
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_section_title
    }
}