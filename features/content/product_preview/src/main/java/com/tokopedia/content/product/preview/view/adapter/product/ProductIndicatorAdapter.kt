package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductIndicatorViewHolder

class ProductIndicatorAdapter(
    private val listener: ProductIndicatorListener
) : ListAdapter<ProductIndicatorUiModel, ViewHolder>(ProductIndicatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductIndicatorViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProductIndicatorViewHolder).bind(getItem(position))
    }

    internal class ProductIndicatorDiffUtil : DiffUtil.ItemCallback<ProductIndicatorUiModel>() {
        override fun areItemsTheSame(
            oldItem: ProductIndicatorUiModel,
            newItem: ProductIndicatorUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductIndicatorUiModel,
            newItem: ProductIndicatorUiModel
        ): Boolean {
            return oldItem.selected == newItem.selected
        }
    }
}
