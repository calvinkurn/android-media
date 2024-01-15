package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.C1VH
import com.tokopedia.gamification.pdp.presentation.viewHolders.C2VH
import com.tokopedia.gamification.pdp.presentation.viewHolders.C3VH
import com.tokopedia.gamification.pdp.presentation.viewHolders.C4VH
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C1VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C2VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C3VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C4VHModel

class KetupatLandingAdapterTypeFactory(/*Listener if needed*/) : BaseAdapterTypeFactory(),
    KetupatLandingTypeFactory {

    override fun type(model: C1VHModel): Int {
        return C1VH.LAYOUT
    }

    override fun type(model: C2VHModel): Int {
        return C2VH.LAYOUT
    }

    override fun type(model: C3VHModel): Int {
        return C3VH.LAYOUT
    }

    override fun type(model: C4VHModel): Int {
        return C4VH.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            C1VH.LAYOUT -> C1VH(parent)
            C2VH.LAYOUT -> C2VH(parent)
            C3VH.LAYOUT -> C3VH(parent)
            C4VH.LAYOUT -> C4VH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
