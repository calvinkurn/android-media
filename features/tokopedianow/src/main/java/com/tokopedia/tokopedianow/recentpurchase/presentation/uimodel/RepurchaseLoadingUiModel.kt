package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory

object RepurchaseLoadingUiModel: Visitable<RecentPurchaseTypeFactory>  {
    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
