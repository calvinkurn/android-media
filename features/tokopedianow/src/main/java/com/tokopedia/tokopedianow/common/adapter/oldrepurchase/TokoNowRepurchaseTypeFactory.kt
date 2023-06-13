package com.tokopedia.tokopedianow.common.adapter.oldrepurchase

import com.tokopedia.tokopedianow.common.model.olderpurchase.TokoNowRepurchaseUiModel

interface TokoNowRepurchaseTypeFactory {
    fun type(uiModel: TokoNowRepurchaseUiModel): Int
}
