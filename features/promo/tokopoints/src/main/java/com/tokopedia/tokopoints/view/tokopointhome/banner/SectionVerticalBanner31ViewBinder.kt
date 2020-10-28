package com.tokopedia.tokopoints.view.tokopointhome.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionVerticalBanner31ViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalBanner31VH>(
        SectionContent::class.java){
    override fun createViewHolder(parent: ViewGroup): SectionVerticalBanner31VH {
        return SectionVerticalBanner31VH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalBanner31VH) {
             viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_banner_3_on_1

}