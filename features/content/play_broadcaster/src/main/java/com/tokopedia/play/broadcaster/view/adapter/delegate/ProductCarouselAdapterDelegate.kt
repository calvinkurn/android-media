package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.carousel.ProductCarouselViewHolder

/**
 * Created by kenny.hadisaputra on 09/02/22
 */
internal class ProductCarouselAdapterDelegate private constructor() {

    class Product(private val listener: ProductCarouselViewHolder.Product.Listener) : BaseAdapterDelegate<
            ProductUiModel, Any, ProductCarouselViewHolder.Product>(commonR.layout.view_play_empty) {

        override fun isForViewType(
            itemList: List<Any>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val product = itemList.filterIsInstance<ProductUiModel>()[position]
            return !product.pinStatus.isPinned
        }

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
            return ProductCarouselViewHolder.Product.create(parent, listener)
        }
    }

    class PinnedProduct(private val pinnedProductListener: ProductCarouselViewHolder.PinnedProduct.Listener): BaseAdapterDelegate<
            ProductUiModel, Any, ProductCarouselViewHolder.PinnedProduct>(commonR.layout.view_play_empty){

        override fun isForViewType(
            itemList: List<Any>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val product = itemList.filterIsInstance<ProductUiModel>()[position]
            return product.pinStatus.isPinned
        }

        override fun onBindViewHolder(
            item: ProductUiModel,
            holder: ProductCarouselViewHolder.PinnedProduct
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductCarouselViewHolder.PinnedProduct = ProductCarouselViewHolder.PinnedProduct.create(parent, listener = pinnedProductListener)

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