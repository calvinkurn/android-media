package com.tokopedia.createpost.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.producttag.view.adapter.LastPurchasedProductAdapter
import com.tokopedia.createpost.producttag.view.adapter.viewholder.LastPurchasedProductViewHolder
import com.tokopedia.createpost.producttag.view.adapter.viewholder.LastTaggedProductViewHolder
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
internal class LastPurchasedProductAdapterDelegate private constructor() {

    internal class Product(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<
            LastPurchasedProductAdapter.Model.Product,
            LastPurchasedProductAdapter.Model,
            LastPurchasedProductViewHolder.Product>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: LastPurchasedProductAdapter.Model.Product,
            holder: LastPurchasedProductViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LastPurchasedProductViewHolder.Product {
            return LastPurchasedProductViewHolder.Product.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            LastPurchasedProductAdapter.Model.Loading,
            LastPurchasedProductAdapter.Model,
            LastPurchasedProductViewHolder.Loading>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: LastPurchasedProductAdapter.Model.Loading,
            holder: LastPurchasedProductViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LastPurchasedProductViewHolder.Loading {
            return LastPurchasedProductViewHolder.Loading.create(parent)
        }
    }
}