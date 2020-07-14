package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageViewHolder
import com.tokopedia.entertainment.pdp.data.Group
import com.tokopedia.entertainment.pdp.data.Package

class PackageTypeFactoryImp: BaseAdapterTypeFactory(), PackageTypeFactory {

    override fun type(dataModel: Package): Int {
        return PackageViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            PackageViewHolder.LAYOUT -> PackageViewHolder(view)
            else -> super.createViewHolder(view, type)
        }

    }
}