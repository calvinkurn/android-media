package com.tokopedia.catalog.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.uimodel.BlankUiModel
import com.tokopedia.catalogcommon.viewholder.BlankViewHolder

class CatalogComparisonDetailAdapterFactoryImpl : BaseAdapterTypeFactory(), CatalogComparisonDetailAdapterFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            BlankViewHolder.LAYOUT -> BlankViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

    override fun type(uiModel: BlankUiModel): Int {
        return BlankViewHolder.LAYOUT
    }
}
