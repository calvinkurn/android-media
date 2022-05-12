package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.carousel.ProductCarouselViewHolder

/**
 * Created by kenny.hadisaputra on 09/02/22
 */
internal class ProductCarouselAdapterDelegate private constructor() {

    class Product : TypedAdapterDelegate<
            ProductUiModel, Any, ProductCarouselViewHolder.Product>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(
            item: ProductUiModel,
            holder: ProductCarouselViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductCarouselViewHolder.Product {
            return ProductCarouselViewHolder.Product.create(parent)
        }
    }

    class Loading : TypedAdapterDelegate<
            Unit, Any, ProductCarouselViewHolder.Loading>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(item: Unit, holder: ProductCarouselViewHolder.Loading) {

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductCarouselViewHolder.Loading {
            return ProductCarouselViewHolder.Loading.create(parent)
        }
    }
}