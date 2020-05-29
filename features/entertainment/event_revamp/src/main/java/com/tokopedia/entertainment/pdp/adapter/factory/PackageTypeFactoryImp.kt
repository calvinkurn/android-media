package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener

class PackageTypeFactoryImp(private val onBindItemTicketListener: OnBindItemTicketListener): BaseAdapterTypeFactory(), PackageTypeFactory {

    override fun type(dataModel: PackageV3): Int {
        return PackageParentViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            PackageParentViewHolder.LAYOUT -> PackageParentViewHolder(view, onBindItemTicketListener)
            else -> super.createViewHolder(view, type)
        }

    }
}