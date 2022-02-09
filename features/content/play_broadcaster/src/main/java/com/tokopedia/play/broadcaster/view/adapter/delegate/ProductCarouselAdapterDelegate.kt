package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroPlaceholderCarouselBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroProductCarouselBinding
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.carousel.ProductCarouselViewHolder

/**
 * Created by kenny.hadisaputra on 09/02/22
 */
internal class ProductCarouselAdapterDelegate private constructor() {

    class Product : TypedAdapterDelegate<
            ProductUiModel, Any, ProductCarouselViewHolder.Product>(R.layout.view_empty) {

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
            return ProductCarouselViewHolder.Product(
                ItemPlayBroProductCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    class Loading : TypedAdapterDelegate<
            Unit, Any, ProductCarouselViewHolder.Loading>(R.layout.view_empty) {

        override fun onBindViewHolder(item: Unit, holder: ProductCarouselViewHolder.Loading) {

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductCarouselViewHolder.Loading {
            return ProductCarouselViewHolder.Loading(
                ItemPlayBroPlaceholderCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}