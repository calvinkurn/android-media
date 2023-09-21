package com.tokopedia.sellerhomecommon.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel

class MultiComponentTabAdapter(
    typeFactory: WidgetAdapterFactoryImpl,
) : BaseAdapter<WidgetAdapterFactoryImpl>(
    typeFactory,
    listOf(LoadingMultiComponentUiModel)
) {

    fun setData(data: List<BaseWidgetUiModel<*>?>) {
        setVisitables(data)
    }
}
