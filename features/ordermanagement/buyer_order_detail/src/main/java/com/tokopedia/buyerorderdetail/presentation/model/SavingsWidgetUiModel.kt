package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.domain.models.PlusComponent
import com.tokopedia.buyerorderdetail.domain.models.PlusTicker

data class SavingsWidgetUiModel(
    val plusTicker: PlusTicker = PlusTicker(),
    val plusComponents: PlusComponent = PlusComponent(),
    val isPlus: Boolean = false
)
