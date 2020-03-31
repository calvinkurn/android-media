package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel

class SetCashbackAdapter(
        adapterTypeFactory: SetCashbackAdapterTypeFactory
) : BaseAdapter<SetCashbackAdapterTypeFactory>(adapterTypeFactory) {

    fun updateCashback(cashbackUiModels: List<SetCashbackUiModel>) {
        visitables.clear()
        visitables.addAll(cashbackUiModels)
        notifyDataSetChanged()
    }
}