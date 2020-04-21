package com.tokopedia.layanan_finansial.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel

class SectionViewHolder(val view: LayananSectionView) : AbstractViewHolder<LayananSectionModel>(view) {
    override fun bind(element: LayananSectionModel) {
        view.setData(element)
    }
    companion object{
         val LAYOUT:Int = R.layout.layanan_section_view
    }
}