package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl

/**
 * @author : Steven 11/04/19
 */

open class NotificationUpdateFilterSectionAdapter(
        typeFactory: NotificationUpdateFilterSectionTypeFactoryImpl)
    :BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(typeFactory) {


    override fun addElement(visitables: List<Visitable<*>>) {
        var position = visitables.size-1
        this.visitables.addAll(visitables)
        notifyItemRangeInserted(position, visitables.size)
    }
}
