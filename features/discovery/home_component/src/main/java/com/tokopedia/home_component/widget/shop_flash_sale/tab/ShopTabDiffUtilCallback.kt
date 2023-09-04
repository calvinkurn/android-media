package com.tokopedia.home_component.widget.shop_flash_sale.tab

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

internal class ShopTabDiffUtilCallback: DiffUtil.ItemCallback<ShopTabDataModel>() {
    override fun areItemsTheSame(
        oldItem: ShopTabDataModel,
        newItem: ShopTabDataModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ShopTabDataModel,
        newItem: ShopTabDataModel
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: ShopTabDataModel,
        newItem: ShopTabDataModel
    ): Any? {
        val bundle = Bundle()
        if(oldItem.isActivated != newItem.isActivated) {
            bundle.putBoolean(ShopTabDataModel.PAYLOAD_ACTIVATED, true)
        }
        return bundle
    }
}
