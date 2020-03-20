package com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.SetCashbackAdapterTypeFactory

class SetCashbackUiModel(
        val description: String = "",
        val cashback: Int = 0,
        val isSelected: Boolean =  false
) : Visitable<SetCashbackAdapterTypeFactory> {

    override fun type(adapterTypeFactory: SetCashbackAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}
