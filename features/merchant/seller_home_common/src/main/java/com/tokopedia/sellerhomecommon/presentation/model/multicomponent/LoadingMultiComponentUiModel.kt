package com.tokopedia.sellerhomecommon.presentation.model.multicomponent

import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel

object LoadingMultiComponentUiModel : MultiComponentItemUiModel {

    override val data: BaseDataUiModel? = null

    override fun type(typeFactory: MultiComponentAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
