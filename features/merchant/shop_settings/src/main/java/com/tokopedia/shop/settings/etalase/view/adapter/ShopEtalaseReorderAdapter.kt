package com.tokopedia.shop.settings.etalase.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseReorderFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopEtalaseReorderAdapter(baseListAdapterTypeFactory: ShopEtalaseReorderFactory) : BaseListAdapter<ShopEtalaseViewModel, ShopEtalaseReorderFactory>(baseListAdapterTypeFactory), ItemTouchHelperAdapter {

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
