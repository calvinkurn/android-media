package com.tokopedia.deals.location_picker.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.location_picker.model.visitor.SectionTitleModel

class LocationSectionTitleViewHolder(itemView: View): AbstractViewHolder<SectionTitleModel>(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_popular_section_title)

    override fun bind(element: SectionTitleModel?) {
        title.text = element?.title
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_section_title_location
    }
}