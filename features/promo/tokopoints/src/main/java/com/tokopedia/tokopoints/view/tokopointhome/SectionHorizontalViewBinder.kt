package com.tokopedia.tokopoints.view.tokopointhome

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent

class SectionHorizontalViewBinder(val block: SectionContent)
    : SectionItemViewBinder<SectionContent, SectionHorizontalViewHolder>(
        SectionContent::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionHorizontalViewHolder {
        return SectionHorizontalViewHolder(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionHorizontalViewHolder) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() : Int= R.layout.tp_layout_generic_carousal

}
