package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.LayoutInflater
import android.view.View
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

    override fun onBindViewHolder(holder: ViewToViewProductViewHolder, position: Int) {
        setOnAttachStateChangeListener(holder)
        holder.bind(getItem(position))
    }

    private fun setOnAttachStateChangeListener(viewHolder: ViewToViewProductViewHolder) {
        val onAttachStateChangeListener: View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                if (viewHolder.bindingAdapterPosition > RecyclerView.NO_POSITION) {
                    viewHolder.onViewAttachedToWindow()
                }
            }

            override fun onViewDetachedFromWindow(view: View) {
                if (viewHolder.bindingAdapterPosition > RecyclerView.NO_POSITION) {
                    viewHolder.onViewDetachedFromWindow(viewHolder.visiblePercentage)
                }
            }
        }
        viewHolder.itemView.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }
}
