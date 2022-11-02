package com.tokopedia.journeydebugger.ui.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel
import com.tokopedia.journeydebugger.ui.viewholder.JourneyDebuggerViewHolder

class JourneyDebuggerTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == JourneyDebuggerViewHolder.LAYOUT) {
            JourneyDebuggerViewHolder(parent)
        } else super.createViewHolder(parent, type)

    }

    fun type(journeyDebuggerUIModel: JourneyDebuggerUIModel): Int {
        return JourneyDebuggerViewHolder.LAYOUT
    }
}
