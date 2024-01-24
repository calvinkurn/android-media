package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.uimodel.product.IndicatorUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductIndicatorViewHolder

class ProductIndicatorAdapter(
    private val listener: ProductIndicatorListener
) : ListAdapter<IndicatorUiModel, ViewHolder>(ProductIndicatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductIndicatorViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProductIndicatorViewHolder).bind(getItem(position))
    }

    internal class ProductIndicatorDiffUtil : DiffUtil.ItemCallback<IndicatorUiModel>() {
        override fun areItemsTheSame(
            oldItem: IndicatorUiModel,
            newItem: IndicatorUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: IndicatorUiModel,
            newItem: IndicatorUiModel
        ): Boolean {
            return oldItem.selected == newItem.selected
        }
    }
}
