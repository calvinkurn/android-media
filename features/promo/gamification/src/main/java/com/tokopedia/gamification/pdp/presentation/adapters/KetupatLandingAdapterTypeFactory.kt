package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.C1VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.C1VH

class KetupatLandingAdapterTypeFactory(/*Listener if needed*/) : BaseAdapterTypeFactory(),
    KetupatLandingTypeFactory {

    override fun type(model: C1VHModel): Int {
        return C1VH.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            C1VH.LAYOUT -> C1VH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
