package com.tokopedia.content.product.picker.seller.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductSummaryViewHolder

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
class ProductSummaryAdapterDelegate private constructor() {

    class Header : TypedAdapterDelegate<
        ProductSummaryAdapter.Model.Header,
        ProductSummaryAdapter.Model,
            ProductSummaryViewHolder.Header>(contentcommonR.layout.view_cc_empty) {

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

    class Body(
        private val listener: ProductSummaryViewHolder.Body.Listener
    ) : TypedAdapterDelegate<
        ProductSummaryAdapter.Model.Body,
        ProductSummaryAdapter.Model,
            ProductSummaryViewHolder.Body>(contentcommonR.layout.view_cc_empty) {

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
