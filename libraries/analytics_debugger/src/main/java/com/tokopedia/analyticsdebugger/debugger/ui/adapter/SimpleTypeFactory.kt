package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ui.model.IdViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.SimpleTextViewHolder

class SimpleTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == SimpleTextViewHolder.LAYOUT) {
            SimpleTextViewHolder(parent)
        } else super.createViewHolder(parent, type)
    }

    fun type(idViewModel: IdViewModel): Int {
        return SimpleTextViewHolder.LAYOUT
    }
}
