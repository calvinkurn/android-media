package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductSummaryViewHolder

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
internal class ProductSummaryAdapterDelegate private constructor() {

    internal class Header : TypedAdapterDelegate<
            ProductSummaryAdapter.Model.Header,
            ProductSummaryAdapter.Model,
            ProductSummaryViewHolder.Header>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: ProductSummaryAdapter.Model.Header,
            holder: ProductSummaryViewHolder.Header
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductSummaryViewHolder.Header {
            return ProductSummaryViewHolder.Header.create(parent)
        }
    }

    internal class Body(
        private val listener: ProductSummaryViewHolder.Body.Listener
    ) : TypedAdapterDelegate<
            ProductSummaryAdapter.Model.Body,
            ProductSummaryAdapter.Model,
            ProductSummaryViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: ProductSummaryAdapter.Model.Body,
            holder: ProductSummaryViewHolder.Body
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductSummaryViewHolder.Body {
            return ProductSummaryViewHolder.Body.create(parent, listener)
        }
    }
}