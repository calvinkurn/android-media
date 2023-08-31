package com.tokopedia.sellerhomecommon.presentation.model.multicomponent

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel

interface MultiComponentItemUiModel: Visitable<MultiComponentAdapterFactory> {

    val data: BaseDataUiModel?

}
