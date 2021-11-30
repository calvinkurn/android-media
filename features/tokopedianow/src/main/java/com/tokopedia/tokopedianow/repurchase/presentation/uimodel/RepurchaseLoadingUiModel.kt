package com.tokopedia.tokopedianow.repurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseTypeFactory

object RepurchaseLoadingUiModel: Visitable<RepurchaseTypeFactory>  {
    override fun type(typeFactory: RepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
