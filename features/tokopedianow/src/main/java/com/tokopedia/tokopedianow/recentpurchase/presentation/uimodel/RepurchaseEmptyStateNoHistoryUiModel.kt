package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory

class RepurchaseEmptyStateNoHistoryUiModel(
    @StringRes val title: Int,
    @StringRes val description: Int
): Visitable<RecentPurchaseTypeFactory> {

    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}