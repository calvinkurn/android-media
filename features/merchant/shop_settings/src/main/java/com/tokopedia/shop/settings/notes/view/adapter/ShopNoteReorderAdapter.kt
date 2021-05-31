package com.tokopedia.shop.settings.notes.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.settings.common.util.ItemTouchHelperAdapter
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteReorderFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopNoteReorderAdapter(baseListAdapterTypeFactory: ShopNoteReorderFactory) : BaseListAdapter<ShopNoteUiModel, ShopNoteReorderFactory>(baseListAdapterTypeFactory), ItemTouchHelperAdapter {

    override fun isItemClickableByDefault(): Boolean {
        return false
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val viewModelList = data
        val modelFrom = viewModelList[fromPosition]
        viewModelList.removeAt(fromPosition)
        viewModelList.add(toPosition, modelFrom)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        // no-op
    }
}
