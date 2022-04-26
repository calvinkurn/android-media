package com.tokopedia.createpost.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.producttag.view.adapter.LastTaggedProductAdapter
import com.tokopedia.createpost.producttag.view.adapter.viewholder.LastTaggedProductViewHolder
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
internal class LastTaggedProductAdapterDelegate private constructor() {

    internal class Product(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<
            LastTaggedProductAdapter.Model.Product,
            LastTaggedProductAdapter.Model,
            LastTaggedProductViewHolder.Product>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: LastTaggedProductAdapter.Model.Product,
            holder: LastTaggedProductViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LastTaggedProductViewHolder.Product {
            return LastTaggedProductViewHolder.Product.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            LastTaggedProductAdapter.Model.Loading,
            LastTaggedProductAdapter.Model,
            LastTaggedProductViewHolder.Loading>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: LastTaggedProductAdapter.Model.Loading,
            holder: LastTaggedProductViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LastTaggedProductViewHolder.Loading {
            return LastTaggedProductViewHolder.Loading.create(parent)
        }
    }
}