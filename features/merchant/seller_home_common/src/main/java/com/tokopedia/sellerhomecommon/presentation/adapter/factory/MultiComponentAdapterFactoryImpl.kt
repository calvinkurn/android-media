package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent.BarMultiComponentViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent.LoadingMultiComponentViewHolder

class MultiComponentAdapterFactoryImpl: BaseAdapterTypeFactory(), MultiComponentAdapterFactory {

    override fun type(uiModel: LoadingMultiComponentUiModel): Int {
        return LoadingMultiComponentViewHolder.RES_LAYOUT
    }

    override fun type(uiModel: BarMultiComponentUiModel): Int {
        return BarMultiComponentViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            LoadingMultiComponentViewHolder.RES_LAYOUT -> LoadingMultiComponentViewHolder(parent)
            BarMultiComponentViewHolder.RES_LAYOUT -> BarMultiComponentViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
