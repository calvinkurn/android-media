package com.tokopedia.tokopoints.view.tokopointhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent

class SectionVerticalColumn311ViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalColumn31VH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SectionVerticalColumn31VH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalColumn31VH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_column_container



}