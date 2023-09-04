package com.tokopedia.home_component.widget.shop_tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_component.util.recordCrashlytics
import java.lang.Exception

internal class ShopFlashSaleTabAdapter(
    diffUtil: ShopTabDiffUtilCallback,
    private val shopTabListener: ShopTabListener? = null,
): ListAdapter<ShopTabDataModel, ShopTabViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopTabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ShopTabViewHolder.LAYOUT, parent, false)
        return ShopTabViewHolder(view, shopTabListener)
    }

    override fun onBindViewHolder(holder: ShopTabViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    override fun onBindViewHolder(
        holder: ShopTabViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        try {
            if((payloads.firstOrNull() as? Bundle)?.getBoolean(ShopTabDataModel.PAYLOAD_ACTIVATED) == true) {
                holder.bindIndicator(getItem(position))
            } else onBindViewHolder(holder, position)
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }
}
