package com.tokopedia.tokopoints.view.tokopointhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent

class SectionVerticalColumn234ViewBinder()
    : SectionItemViewBinder<SectionContent, SectionVerticalCoulmn11VH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SectionVerticalCoulmn11VH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionVerticalCoulmn11VH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_column_container234

}