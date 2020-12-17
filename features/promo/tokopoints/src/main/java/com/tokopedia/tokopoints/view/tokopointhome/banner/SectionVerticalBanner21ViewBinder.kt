package com.tokopedia.tokopoints.view.tokopointhome.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionVerticalBanner21ViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalBanner21VH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionVerticalBanner21VH {
        return (
                SectionVerticalBanner21VH(LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false)))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalBanner21VH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType(): Int = R.layout.tp_layout_banner_2_on_1

}