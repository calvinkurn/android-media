package com.tokopedia.notifcenter.presentation.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.notifcenter.util.resize

class MultipleProductCardAdapter(
        private val multipleProductCardFactory: MultipleProductCardFactoryImpl,
        private val isResizable: Boolean = false
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(multipleProductCardFactory) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val containerView = onCreateViewItem(parent, viewType)
        if (isResizable) containerView.resize(80)
        return multipleProductCardFactory.createViewHolder(containerView, viewType)
    }

    fun removeAllItem() {
        visitables.removeAll {
            it is MultipleProductCardViewBean
        }
        notifyDataSetChanged()
    }

    fun insertData(data: List<MultipleProductCardViewBean>) {
        addElement(data)
    }

}