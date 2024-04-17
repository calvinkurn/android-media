package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding


class ViewToViewAdapter(
    private val listener: ViewToViewListener,
    diffUtilItemCallback: ViewToViewDiffUtilItemCallback = ViewToViewDiffUtilItemCallback(),
) : ListAdapter<ViewToViewDataModel, ViewToViewProductViewHolder>(
    diffUtilItemCallback
) {

    private val percentageScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewToViewProductViewHolder {
        val view = ItemViewToViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewToViewProductViewHolder(listener, view.root)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(percentageScrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(percentageScrollListener)
    }

    override fun onViewAttachedToWindow(holder: ViewToViewProductViewHolder) {
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: ViewToViewProductViewHolder) {
        holder.onViewDetachedFromWindow(holder.visiblePercentage)
    }

    override fun onBindViewHolder(holder: ViewToViewProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
