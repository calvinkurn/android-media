package com.tokopedia.tokopedianow.repurchase.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseTypeFactory

class RepurchaseEmptyStateNoHistoryUiModel(
    @StringRes val title: Int,
    @StringRes val description: Int
): Visitable<RepurchaseTypeFactory> {

    override fun type(typeFactory: RepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}