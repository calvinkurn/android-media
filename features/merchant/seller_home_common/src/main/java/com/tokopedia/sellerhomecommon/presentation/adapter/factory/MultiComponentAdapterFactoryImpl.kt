package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent.LoadingMultiComponentViewHolder

class MultiComponentAdapterFactoryImpl: BaseAdapterTypeFactory(), MultiComponentAdapterFactory {

    override fun type(uiModel: LoadingMultiComponentUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        super.createViewHolder(parent, type)
        return LoadingMultiComponentViewHolder(parent)
    }

}
