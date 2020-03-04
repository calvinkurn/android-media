package com.tokopedia.centralized_promo.view.fragment.partialview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapter
import com.tokopedia.centralized_promo.view.model.BaseUiModel

abstract class PartialView<T : BaseUiModel, F : BaseAdapterTypeFactory, V : Visitable<F>>(typeFactory: F) {
    protected val adapter by lazy {
        CentralizedPromoAdapter<V, F>(typeFactory)
    }

    abstract fun renderData(data: T)
    abstract fun renderError(cause: Throwable)
    abstract fun onRefresh()
}