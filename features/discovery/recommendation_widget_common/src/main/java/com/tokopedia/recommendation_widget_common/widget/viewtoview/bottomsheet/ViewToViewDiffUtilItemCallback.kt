package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import androidx.recyclerview.widget.DiffUtil

class ViewToViewDiffUtilItemCallback : DiffUtil.ItemCallback<ViewToViewDataModel>() {
    override fun areItemsTheSame(
        oldItem: ViewToViewDataModel,
        newItem: ViewToViewDataModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ViewToViewDataModel,
        newItem: ViewToViewDataModel
    ): Boolean {
        return oldItem == newItem
    }
}
