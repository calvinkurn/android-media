package com.tokopedia.bmsm_widget.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel

/**
 * Created by @ilhamsuaib on 07/12/23.
 */

object ProductGiftDiffUtilCallback {

    fun createDiffUtil(): DiffUtil.ItemCallback<ProductGiftUiModel> {
        return object : DiffUtil.ItemCallback<ProductGiftUiModel>() {

            override fun areItemsTheSame(
                oldItem: ProductGiftUiModel,
                newItem: ProductGiftUiModel
            ): Boolean {
                return newItem.id == oldItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductGiftUiModel,
                newItem: ProductGiftUiModel
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}