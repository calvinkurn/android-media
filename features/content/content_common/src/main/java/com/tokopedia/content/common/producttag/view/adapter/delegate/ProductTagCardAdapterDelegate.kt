package com.tokopedia.content.common.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.content.common.producttag.view.adapter.viewholder.ProductTagCardViewHolder
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

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
        private val onSelected: (ProductUiModel, Int) -> Unit,
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

    internal class ProductWithCheckbox(
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : TypedAdapterDelegate<
            ProductTagCardAdapter.Model.ProductWithCheckbox,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.ProductWithCheckbox>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.ProductWithCheckbox,
            holder: ProductTagCardViewHolder.ProductWithCheckbox
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.ProductWithCheckbox {
            return ProductTagCardViewHolder.ProductWithCheckbox.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            ProductTagCardAdapter.Model.Loading,
            ProductTagCardAdapter.Model,
            LoadingViewHolder>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.Loading,
            holder: LoadingViewHolder
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LoadingViewHolder {
            return LoadingViewHolder.create(parent)
        }
    }

    internal class EmptyState: TypedAdapterDelegate<
            ProductTagCardAdapter.Model.EmptyState,
            ProductTagCardAdapter.Model,
            ProductTagCardViewHolder.EmptyState>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ProductTagCardAdapter.Model.EmptyState,
            holder: ProductTagCardViewHolder.EmptyState
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductTagCardViewHolder.EmptyState {
            return ProductTagCardViewHolder.EmptyState.create(
                parent
            )
        }
    }
}