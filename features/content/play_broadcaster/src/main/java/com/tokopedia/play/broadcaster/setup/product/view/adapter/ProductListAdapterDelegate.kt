package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductListViewHolder
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
internal class ProductListAdapterDelegate private constructor() {

    internal class Product(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<
            ProductListAdapter.Model.Product,
            ProductListAdapter.Model,
            ProductListViewHolder.Product>(
        R.layout.view_empty) {

        override fun onBindViewHolder(
            item: ProductListAdapter.Model.Product,
            holder: ProductListViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductListViewHolder.Product {
            return ProductListViewHolder.Product.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            ProductListAdapter.Model.Loading,
            ProductListAdapter.Model,
            ProductListViewHolder.Loading>(
        R.layout.view_empty) {

        override fun onBindViewHolder(
            item: ProductListAdapter.Model.Loading,
            holder: ProductListViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductListViewHolder.Loading {
            return ProductListViewHolder.Loading.create(parent)
        }
    }
}