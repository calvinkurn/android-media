package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

object AdapterDiffCallback {

    val WidgetDiffCallback = object : DiffUtil.ItemCallback<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem>() {
        override fun areItemsTheSame(oldItemShopHomeDisplay: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, newItemShopHomeDisplay: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem): Boolean {
            return oldItemShopHomeDisplay == newItemShopHomeDisplay
        }

        override fun areContentsTheSame(oldItemShopHomeDisplay: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, newItemShopHomeDisplay: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem): Boolean {
            return oldItemShopHomeDisplay == newItemShopHomeDisplay
        }
    }
}
