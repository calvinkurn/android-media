package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.FpmDebuggerViewHolder

/**
 * @author okasurya on 5/16/18.
 */
class FpmDebuggerTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == FpmDebuggerViewHolder.LAYOUT) {
            FpmDebuggerViewHolder(parent)
        } else super.createViewHolder(parent, type)

    }

    fun type(fpmDebuggerViewModel: FpmDebuggerViewModel): Int {
        return FpmDebuggerViewHolder.LAYOUT
    }
}
