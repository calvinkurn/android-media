package com.tokopedia.layanan_finansial.view.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel

class SectionViewHolder(val view: LayananSectionView) : AbstractViewHolder<Visitable<*>>(view) {

    companion object{
         val LAYOUT:Int = R.layout.layanan_section_view
    }

    override fun bind(element: Visitable<*>?) {
        view.setData(element as LayananSectionModel)
    }
}