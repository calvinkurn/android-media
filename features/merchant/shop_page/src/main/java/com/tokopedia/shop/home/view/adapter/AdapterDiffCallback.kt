package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.shop.home.view.model.WidgetDataModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

object AdapterDiffCallback {

    val WidgetDiffCallback = object : DiffUtil.ItemCallback<WidgetDataModel>() {
        override fun areItemsTheSame(oldItem: WidgetDataModel, newItem: WidgetDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WidgetDataModel, newItem: WidgetDataModel): Boolean {
            return oldItem == newItem
        }
    }
}