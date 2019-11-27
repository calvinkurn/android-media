package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class PdpGamificationAdapter(val typeFactory: PdpGamificationAdapterTypeFactory,
                             visitables: List<Visitable<Any>>) :
        BaseAdapter<PdpGamificationAdapterTypeFactory>(typeFactory, visitables) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)

    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        super.onBindViewHolder(holder, position)
    }

}