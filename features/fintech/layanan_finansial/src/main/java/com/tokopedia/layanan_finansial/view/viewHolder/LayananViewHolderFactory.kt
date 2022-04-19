package com.tokopedia.layanan_finansial.view.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.layanan_finansial.view.utils.ViewType

class LayananViewHolderFactory : BaseAdapterTypeFactory(),ViewType{

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        if (type == SectionViewHolder.LAYOUT){
            return SectionViewHolder(LayananSectionView(parent.context))
        }
        else if(type == TopAdsViewHolder.LAYOUT)
        {
            return TopAdsViewHolder(view = parent)
        }
        return super.createViewHolder(parent, type)
    }

    override fun type(dataModel: LayananSectionModel): Int {
        return SectionViewHolder.LAYOUT
    }

    override fun type(dataModel: TopAdsImageModel): Int {
       return TopAdsViewHolder.LAYOUT
    }
}