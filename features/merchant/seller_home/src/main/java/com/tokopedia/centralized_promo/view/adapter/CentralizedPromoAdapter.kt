package com.tokopedia.centralized_promo.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

class CentralizedPromoAdapter<T : Visitable<F>, F : BaseAdapterTypeFactory>(typeFactory: F)
    : BaseListAdapter<T, F>(typeFactory) {

    override fun setElements(data: List<Visitable<*>>) {
        val result = DiffUtilHelper.calculateDiffInBackground(visitables, data)
        visitables = data
        result.dispatchUpdatesTo(this)
    }
}