package com.tokopedia.smartbills.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder

/**
 * @author by resakemal on 21/04/20
 */

class SmartBillsAdapterFactory(private val checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                               private val detailListener: SmartBillsViewHolder.DetailListener):
        BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<RechargeBills> {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SmartBillsViewHolder.LAYOUT -> SmartBillsViewHolder(parent, checkableListener, detailListener)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(item: RechargeBills): Int {
        return SmartBillsViewHolder.LAYOUT
    }

}