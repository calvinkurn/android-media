package com.tokopedia.sellerorder.list.presentation.adapter.typefactories

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListBulkAcceptOrderProductViewHolder
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderProductUiModel

class SomListBulkAcceptOrderTypeFactory: BaseAdapterTypeFactory() {
    fun type(somListBulkAcceptOrderProductUiModel: SomListBulkAcceptOrderProductUiModel): Int {
        return SomListBulkAcceptOrderProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SomListBulkAcceptOrderProductViewHolder.LAYOUT -> SomListBulkAcceptOrderProductViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}