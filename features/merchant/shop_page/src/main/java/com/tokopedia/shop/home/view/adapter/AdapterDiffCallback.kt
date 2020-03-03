package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

object AdapterDiffCallback {

    val WidgetDiffCallback = object : DiffUtil.ItemCallback<DisplayWidgetUiModel.DisplayWidgetItem>() {
        override fun areItemsTheSame(oldItemDisplay: DisplayWidgetUiModel.DisplayWidgetItem, newItemDisplay: DisplayWidgetUiModel.DisplayWidgetItem): Boolean {
            return oldItemDisplay == newItemDisplay
        }

        override fun areContentsTheSame(oldItemDisplay: DisplayWidgetUiModel.DisplayWidgetItem, newItemDisplay: DisplayWidgetUiModel.DisplayWidgetItem): Boolean {
            return oldItemDisplay == newItemDisplay
        }
    }
}