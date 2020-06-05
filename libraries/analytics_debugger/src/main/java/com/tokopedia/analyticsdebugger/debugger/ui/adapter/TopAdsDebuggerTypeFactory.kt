package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.TopAdsDebuggerViewHolder

/**
 * @author okasurya on 5/16/18.
 */
class TopAdsDebuggerTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == TopAdsDebuggerViewHolder.LAYOUT) {
            TopAdsDebuggerViewHolder(parent)
        } else super.createViewHolder(parent, type)

    }

    fun type(topAdsDebuggerViewModel: TopAdsDebuggerViewModel): Int {
        return TopAdsDebuggerViewHolder.LAYOUT
    }
}
