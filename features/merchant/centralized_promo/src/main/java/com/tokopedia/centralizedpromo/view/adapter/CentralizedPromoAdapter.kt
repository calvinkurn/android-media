package com.tokopedia.centralizedpromo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CentralizedPromoAdapter<T : Visitable<F>, F : BaseAdapterTypeFactory>(typeFactory: F, val onResultDispatched: () -> Unit)
    : BaseListAdapter<T, F>(typeFactory) {

    override fun setElements(data: List<Visitable<*>>) {
        DiffUtilHelper.calculateDiffInBackground(visitables, data, ::dispatchDiffUtilResult)
    }

    private suspend fun dispatchDiffUtilResult(data: List<Visitable<*>>, result: DiffUtil.DiffResult) = withContext(Dispatchers.Main) {
        visitables = data
        result.dispatchUpdatesTo(this@CentralizedPromoAdapter)
        onResultDispatched()
    }
}
