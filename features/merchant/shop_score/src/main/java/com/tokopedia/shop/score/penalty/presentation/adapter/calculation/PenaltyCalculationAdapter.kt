package com.tokopedia.shop.score.penalty.presentation.adapter.calculation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class PenaltyCalculationAdapter(
    uiModels: List<Visitable<*>>,
    adapterFactory: PenaltyCalculationAdapterFactory
) : BaseListAdapter<Visitable<*>, PenaltyCalculationAdapterFactory>(adapterFactory) {

    init {
        visitables.clear()
        visitables.addAll(uiModels)
        notifyDataSetChanged()
    }

}
