package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.diffutil.SetCashbackDiffUtil
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel

class SetCashbackAdapter(
        adapterTypeFactory: SetCashbackAdapterTypeFactory
) : BaseAdapter<SetCashbackAdapterTypeFactory>(adapterTypeFactory) {

    fun updateCashback(cashbackUiModels: List<SetCashbackUiModel>) {
        val diffUtilCallback = SetCashbackDiffUtil(visitables.filterIsInstance<SetCashbackUiModel>(), cashbackUiModels)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(cashbackUiModels)
        result.dispatchUpdatesTo(this)
    }
}