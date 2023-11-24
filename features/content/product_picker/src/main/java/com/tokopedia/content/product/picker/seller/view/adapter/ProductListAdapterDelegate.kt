package com.tokopedia.content.product.picker.seller.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductListViewHolder
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductListAdapterDelegate private constructor() {

    class Product(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<
        ProductListAdapter.Model.Product,
        ProductListAdapter.Model,
            ProductListViewHolder.Product>(
        contentcommonR.layout.view_cc_empty) {

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

    class Loading : TypedAdapterDelegate<
        ProductListAdapter.Model.Loading,
        ProductListAdapter.Model,
            ProductListViewHolder.Loading>(
        contentcommonR.layout.view_cc_empty) {

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
