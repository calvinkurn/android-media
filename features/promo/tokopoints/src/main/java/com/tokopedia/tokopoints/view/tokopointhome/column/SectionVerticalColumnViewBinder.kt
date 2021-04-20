package com.tokopedia.tokopoints.view.tokopointhome.column

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionVerticalColumnViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalColumnVH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionVerticalColumnVH {
        return SectionVerticalColumnVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalColumnVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_column_container234

}