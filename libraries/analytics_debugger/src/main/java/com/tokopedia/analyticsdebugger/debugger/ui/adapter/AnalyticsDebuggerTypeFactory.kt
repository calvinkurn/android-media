package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.AnalyticsDebuggerViewHolder

/**
 * @author okasurya on 5/16/18.
 */
class AnalyticsDebuggerTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == AnalyticsDebuggerViewHolder.LAYOUT) {
            AnalyticsDebuggerViewHolder(parent)
        } else super.createViewHolder(parent, type)

    }

    fun type(analyticsDebuggerViewModel: AnalyticsDebuggerViewModel): Int {
        return AnalyticsDebuggerViewHolder.LAYOUT
    }
}
