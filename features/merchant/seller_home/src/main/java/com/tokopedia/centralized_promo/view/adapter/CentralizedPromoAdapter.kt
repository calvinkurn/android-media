package com.tokopedia.centralized_promo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CentralizedPromoAdapter<T : Visitable<F>, F : BaseAdapterTypeFactory>(typeFactory: F)
    : BaseListAdapter<T, F>(typeFactory) {

    override fun setElements(data: List<Visitable<*>>) {
        DiffUtilHelper.calculateDiffInBackground(visitables, data, ::dispatchResult)
    }

    private suspend fun dispatchResult(data: List<Visitable<*>>, result: DiffUtil.DiffResult) = withContext(Dispatchers.Main) {
        visitables = data
        result.dispatchUpdatesTo(this@CentralizedPromoAdapter)
    }
}