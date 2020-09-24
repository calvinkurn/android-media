package com.tokopedia.tokopoints.view.tokopointhome.column

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionVerticalColumn234ViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalColumn11VH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionVerticalColumn11VH {
        return SectionVerticalColumn11VH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalColumn11VH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_column_container234

}