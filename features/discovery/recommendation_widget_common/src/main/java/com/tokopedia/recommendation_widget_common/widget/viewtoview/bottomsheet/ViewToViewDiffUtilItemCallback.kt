package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import androidx.recyclerview.widget.DiffUtil

class ViewToViewDiffUtilItemCallback : DiffUtil.ItemCallback<ViewToViewDataModel>() {
    override fun areItemsTheSame(
        oldItem: ViewToViewDataModel,
        newItem: ViewToViewDataModel
    ): Boolean {
        val isSameClass = oldItem.javaClass == newItem.javaClass
        val isSameProductId =
            if (oldItem is ViewToViewDataModel.Product && newItem is ViewToViewDataModel.Product) {
                oldItem.id == newItem.id
            } else false
        return isSameClass || isSameProductId
    }

    override fun areContentsTheSame(
        oldItem: ViewToViewDataModel,
        newItem: ViewToViewDataModel
    ): Boolean {
        return oldItem == newItem
    }
}
