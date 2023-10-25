package com.tokopedia.sellerhomecommon.presentation.model.multicomponent

import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactory

object LoadingMultiComponentUiModel : MultiComponentItemUiModel {

    override fun type(typeFactory: MultiComponentAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
