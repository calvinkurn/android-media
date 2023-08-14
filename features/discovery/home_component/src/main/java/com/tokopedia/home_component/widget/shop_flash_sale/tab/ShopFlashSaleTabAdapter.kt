package com.tokopedia.home_component.widget.shop_flash_sale.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_component.util.recordCrashlytics
import java.lang.Exception

internal class ShopFlashSaleTabAdapter(
    diffUtil: ShopFlashSaleTabDiffUtil,
    private val shopFlashSaleTabListener: ShopFlashSaleTabListener,
): ListAdapter<ShopFlashSaleTabDataModel, ShopFlashSaleTabViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopFlashSaleTabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ShopFlashSaleTabViewHolder.LAYOUT, parent, false)
        return ShopFlashSaleTabViewHolder(view, shopFlashSaleTabListener)
    }

    override fun onBindViewHolder(holder: ShopFlashSaleTabViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    override fun onBindViewHolder(
        holder: ShopFlashSaleTabViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        try {
            if((payloads.firstOrNull() as? Bundle)?.getBoolean(ShopFlashSaleTabDataModel.PAYLOAD_ACTIVATED) == true) {
                holder.bindIndicator(getItem(position))
            } else onBindViewHolder(holder, position)
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }
}
