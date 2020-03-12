package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackViewModel

class SetCashbackAdapter(
        adapterTypeFactory: SetCashbackAdapterTypeFactory
) : BaseAdapter<SetCashbackAdapterTypeFactory>(adapterTypeFactory) {

    fun updateCashback(cashbackViewModels: List<SetCashbackViewModel>) {
        visitables.clear()
        visitables.addAll(cashbackViewModels)
        notifyDataSetChanged()
    }
}