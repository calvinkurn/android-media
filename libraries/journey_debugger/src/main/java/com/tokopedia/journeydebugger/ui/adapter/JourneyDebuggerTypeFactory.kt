package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.ApplinkDebuggerViewHolder

class JourneyDebuggerTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == JourneyDebuggerViewHolder.LAYOUT) {
            JourneyDebuggerViewHolder(parent)
        } else super.createViewHolder(parent, type)

    }

    fun type(journeyDebuggerViewModel: JourneyDebuggerViewModel): Int {
        return JourneyDebuggerViewHolder.LAYOUT
    }
}
