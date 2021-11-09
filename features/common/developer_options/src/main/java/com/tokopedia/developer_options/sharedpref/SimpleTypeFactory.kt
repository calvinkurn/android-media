package com.tokopedia.developer_options.sharedpref

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

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
