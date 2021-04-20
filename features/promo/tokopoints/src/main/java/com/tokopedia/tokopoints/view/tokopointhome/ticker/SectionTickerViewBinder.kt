package com.tokopedia.tokopoints.view.tokopointhome.ticker

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionTickerViewBinder()
    : SectionItemViewBinder<SectionContent, SectionTickerViewHolder>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionTickerViewHolder {
        return SectionTickerViewHolder(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionTickerViewHolder) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_section_ticker_new

}