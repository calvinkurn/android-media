package com.tokopedia.content.product.preview.view.adapter.product

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductIndicatorViewHolder

class ProductIndicatorAdapter(
    listener: ProductIndicatorListener
) : BaseDiffUtilAdapter<ProductIndicatorUiModel>() {

    init {
        delegatesManager.addDelegate(ProductIndicatorDelegate.ProductIndicator(listener))
    }

    fun insertData(data: List<ProductIndicatorUiModel>) {
        clearAllItems()
        setItems(data)
        notifyItemRangeChanged(0, data.size)
    }

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

    internal class ProductIndicatorDelegate private constructor() {

        internal class ProductIndicator(private val listener: ProductIndicatorListener) :
            TypedAdapterDelegate<
                ProductIndicatorUiModel,
                ProductIndicatorUiModel,
                ProductIndicatorViewHolder>(R.layout.item_product_indicator) {

            override fun onBindViewHolder(
                item: ProductIndicatorUiModel,
                holder: ProductIndicatorViewHolder
            ) {
                holder.bind(item)
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                basicView: View
            ): ProductIndicatorViewHolder {
                return ProductIndicatorViewHolder.create(parent, listener)
            }
        }
    }
}
