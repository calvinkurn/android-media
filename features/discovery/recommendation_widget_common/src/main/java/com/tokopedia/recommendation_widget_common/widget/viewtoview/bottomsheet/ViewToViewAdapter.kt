package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding


class ViewToViewAdapter(
    private val listener: ViewToViewListener,
    diffUtilItemCallback: ViewToViewDiffUtilItemCallback = ViewToViewDiffUtilItemCallback(),
) : ListAdapter<ViewToViewDataModel, ViewToViewProductViewHolder>(
    diffUtilItemCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewToViewProductViewHolder {
        val view = ItemViewToViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewToViewProductViewHolder(listener, view.root)
    }

    override fun onBindViewHolder(holder: ViewToViewProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
