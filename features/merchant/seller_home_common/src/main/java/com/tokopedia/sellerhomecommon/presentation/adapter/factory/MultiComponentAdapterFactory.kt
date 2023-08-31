package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel

interface MultiComponentAdapterFactory: AdapterTypeFactory {

    fun type(uiModel: LoadingMultiComponentUiModel): Int

}
