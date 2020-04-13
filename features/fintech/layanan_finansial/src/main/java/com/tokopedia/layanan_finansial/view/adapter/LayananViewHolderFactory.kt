package com.tokopedia.layanan_finansial.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel

class LayananViewHolderFactory : BaseAdapterTypeFactory(){

    fun type(viewModel: LayananSectionModel): Int {
        return SectionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        if (type == SectionViewHolder.LAYOUT){
            return SectionViewHolder(LayananSectionView(parent.context))
        }
        return super.createViewHolder(parent, type)
    }
}