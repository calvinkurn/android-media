package com.tokopedia.sellerhomecommon.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactory
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.MultiComponentItemUiModel

class MultiComponentTabAdapter(
    typeFactory: MultiComponentAdapterFactory,
) : BaseAdapter<MultiComponentAdapterFactory>(
    typeFactory,
    listOf<MultiComponentItemUiModel>(LoadingMultiComponentUiModel)
) {

    fun setData(data: List<MultiComponentItemUiModel>) {
        setVisitables(data)
    }

    fun setLoading() {
        setVisitables(
            listOf(LoadingMultiComponentUiModel)
        )
    }



}
