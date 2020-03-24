package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackViewModel

interface SetCashbackTypeFactory {
    fun type(setCashbackViewModel: SetCashbackViewModel): Int
}