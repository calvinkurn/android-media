package com.tokopedia.catalog.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.ui.uimodel.TopFeaturesUiModel
import com.tokopedia.catalog.ui.viewHolder.TopFeatureViewHolder


class CatalogAdapterFactoryImpl : BaseAdapterTypeFactory(), CatalogAdapterFactory {


    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopFeatureViewHolder.LAYOUT -> TopFeatureViewHolder(
                view
            )
            else -> super.createViewHolder(view, type)
        }

    }

    override fun type(uiModel: TopFeaturesUiModel): Int {
        return TopFeatureViewHolder.LAYOUT
    }
}
