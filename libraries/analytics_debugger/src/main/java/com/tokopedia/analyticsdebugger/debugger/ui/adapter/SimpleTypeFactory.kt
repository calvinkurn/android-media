package com.tokopedia.analyticsdebugger.debugger.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.sharedpref.IdViewModel
import com.tokopedia.developer_options.sharedpref.SimpleTextViewHolder

class SimpleTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == com.tokopedia.developer_options.sharedpref.SimpleTextViewHolder.LAYOUT) {
            com.tokopedia.developer_options.sharedpref.SimpleTextViewHolder(parent)
        } else super.createViewHolder(parent, type)
    }

    fun type(idViewModel: com.tokopedia.developer_options.sharedpref.IdViewModel): Int {
        return com.tokopedia.developer_options.sharedpref.SimpleTextViewHolder.LAYOUT
    }
}
