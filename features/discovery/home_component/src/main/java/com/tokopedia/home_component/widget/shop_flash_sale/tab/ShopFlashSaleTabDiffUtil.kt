package com.tokopedia.home_component.widget.shop_flash_sale.tab

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil

internal class ShopFlashSaleTabDiffUtil: DiffUtil.ItemCallback<ShopFlashSaleTabDataModel>() {
    override fun areItemsTheSame(
        oldItem: ShopFlashSaleTabDataModel,
        newItem: ShopFlashSaleTabDataModel
    ): Boolean = oldItem.channelGrid.id == newItem.channelGrid.id

    override fun areContentsTheSame(
        oldItem: ShopFlashSaleTabDataModel,
        newItem: ShopFlashSaleTabDataModel
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: ShopFlashSaleTabDataModel,
        newItem: ShopFlashSaleTabDataModel
    ): Any? {
        val bundle = Bundle()
        if(oldItem.isActivated != newItem.isActivated) {
            bundle.putBoolean(ShopFlashSaleTabDataModel.PAYLOAD_ACTIVATED, true)
        }
        return bundle
    }
}
