package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

object AdapterDiffCallback {

    val WidgetDiffCallback = object : DiffUtil.ItemCallback<DisplayWidgetUiModel.WidgetItem>() {
        override fun areItemsTheSame(oldItem: DisplayWidgetUiModel.WidgetItem, newItem: DisplayWidgetUiModel.WidgetItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DisplayWidgetUiModel.WidgetItem, newItem: DisplayWidgetUiModel.WidgetItem): Boolean {
            return oldItem == newItem
        }

    }
}