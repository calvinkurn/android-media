package com.tokopedia.statistic.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.MultiComponentViewHolder

class StatisticAdapter(factory: WidgetAdapterFactoryImpl, private val hideCoachMark: () -> Unit) :
    BaseListAdapter<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(factory) {

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)

        val holder = holder as? MultiComponentViewHolder

        if (holder != null) {
            hideCoachMark.invoke()
        }
    }
}