package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductIndicatorViewHolder

class ProductIndicatorAdapter(
    private val productIndicatorListener: ProductIndicatorListener
) : ListAdapter<ProductContentUiModel, ViewHolder>(ProductIndicatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductIndicatorViewHolder.create(parent, productIndicatorListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProductIndicatorViewHolder).bind(getItem(position))
    }

    internal class ProductIndicatorDiffUtil : DiffUtil.ItemCallback<ProductContentUiModel>() {
        override fun areItemsTheSame(
            oldItem: ProductContentUiModel,
            newItem: ProductContentUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductContentUiModel,
            newItem: ProductContentUiModel
        ): Boolean {
            return oldItem.selected == newItem.selected
        }
    }
}
