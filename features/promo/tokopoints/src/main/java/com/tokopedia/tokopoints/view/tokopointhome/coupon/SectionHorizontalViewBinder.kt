package com.tokopedia.tokopoints.view.tokopointhome.coupon

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionHorizontalViewBinder()
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
