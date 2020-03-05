package com.tokopedia.notifcenter.presentation.adapter.typefactory.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.MultipleProductCardViewHolder

class MultipleProductCardFactoryImpl(
        private val sourceView: SourceMultipleProductView,
        private val listener: NotificationItemListener
): BaseAdapterTypeFactory(), MultipleProductCardFactory {

    override fun type(multipleProduct: MultipleProductCardViewBean): Int {
        return MultipleProductCardViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            MultipleProductCardViewHolder.LAYOUT -> MultipleProductCardViewHolder(parent, sourceView, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

}