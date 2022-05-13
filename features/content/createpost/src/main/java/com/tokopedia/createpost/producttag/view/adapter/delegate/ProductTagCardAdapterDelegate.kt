package com.tokopedia.createpost.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.adapter.viewholder.ProductTagCardViewHolder
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
internal class ProductTagCardAdapterDelegate private constructor() {

    internal class Suggestion: TypedAdapterDelegate<
            ProductTagCardAdapter.Model.Suggestion,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.Suggestion>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.Suggestion,
            holder: ProductTagCardViewHolder.Suggestion
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.Suggestion {
            return ProductTagCardViewHolder.Suggestion.create(
                parent
            )
        }
    }

    internal class Ticker: TypedAdapterDelegate<
            ProductTagCardAdapter.Model.Ticker,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.Ticker>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.Ticker,
            holder: ProductTagCardViewHolder.Ticker
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.Ticker {
            return ProductTagCardViewHolder.Ticker.create(
                parent
            )
        }
    }

    internal class Product(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<
            ProductTagCardAdapter.Model.Product,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.Product>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.Product,
            holder: ProductTagCardViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.Product {
            return ProductTagCardViewHolder.Product.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            ProductTagCardAdapter.Model.Loading,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.Loading>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.Loading,
            holder: ProductTagCardViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.Loading {
            return ProductTagCardViewHolder.Loading.create(parent)
        }
    }
}