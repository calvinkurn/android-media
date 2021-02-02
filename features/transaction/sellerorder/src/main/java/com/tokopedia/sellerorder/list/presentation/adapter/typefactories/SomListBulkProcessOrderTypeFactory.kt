package com.tokopedia.sellerorder.list.presentation.adapter.typefactories

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListBulkProcessOrderProductViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListBulkProcessOrderDescriptionViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListBulkProcessOrderMenuItemViewHolder
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkProcessOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderProductUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderDescriptionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderMenuItemUiModel

class SomListBulkProcessOrderTypeFactory(
        var menuItemListener: SomListBulkProcessOrderBottomSheet.SomListBulkProcessOrderBottomSheetListener?
): BaseAdapterTypeFactory() {
    fun type(somListBulkProcessOrderProductUiModel: SomListBulkProcessOrderProductUiModel): Int {
        return SomListBulkProcessOrderProductViewHolder.LAYOUT
    }

    fun type(somListBulkProcessOrderMenuItemUiModel: SomListBulkProcessOrderMenuItemUiModel): Int {
        return SomListBulkProcessOrderMenuItemViewHolder.LAYOUT
    }

    fun type(somListBulkProcessOrderDescriptionUiModel: SomListBulkProcessOrderDescriptionUiModel): Int {
        return SomListBulkProcessOrderDescriptionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SomListBulkProcessOrderProductViewHolder.LAYOUT -> SomListBulkProcessOrderProductViewHolder(parent)
            SomListBulkProcessOrderMenuItemViewHolder.LAYOUT -> SomListBulkProcessOrderMenuItemViewHolder(parent, menuItemListener)
            SomListBulkProcessOrderDescriptionViewHolder.LAYOUT -> SomListBulkProcessOrderDescriptionViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}