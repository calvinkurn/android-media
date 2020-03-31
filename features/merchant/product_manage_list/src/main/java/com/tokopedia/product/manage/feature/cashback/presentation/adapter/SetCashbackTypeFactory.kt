package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel

interface SetCashbackTypeFactory {
    fun type(setCashbackUiModel: SetCashbackUiModel): Int
}