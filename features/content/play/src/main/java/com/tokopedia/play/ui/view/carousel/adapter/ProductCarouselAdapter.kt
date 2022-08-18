package com.tokopedia.play.ui.view.carousel.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.delegate.ProductFeaturedPlaceholderAdapterDelegate
import com.tokopedia.play.ui.view.carousel.viewholder.ProductCarouselViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created by kenny.hadisaputra on 08/07/22
 */
class ProductCarouselAdapter(
    listener: ProductBasicViewHolder.Listener,
    pinnedProductListener: ProductCarouselViewHolder.PinnedProduct.Listener,
) : BaseDiffUtilAdapter<PlayProductUiModel>() {

    init {
        delegatesManager
            .addDelegate(ProductDelegate(listener))
            .addDelegate(PinnedProductDelegate(pinnedProductListener))
            .addDelegate(ProductFeaturedPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: PlayProductUiModel,
        newItem: PlayProductUiModel
    ): Boolean {
        return if (oldItem is PlayProductUiModel.Product && newItem is PlayProductUiModel.Product) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayProductUiModel,
        newItem: PlayProductUiModel
    ): Boolean {
        return oldItem == newItem
    }

    class ProductDelegate(
        private val listener: ProductBasicViewHolder.Listener,
    ) : BaseAdapterDelegate<PlayProductUiModel.Product, PlayProductUiModel, ProductFeaturedViewHolder>(
        commonR.layout.view_play_empty
    ) {

        override fun isForViewType(
            itemList: List<PlayProductUiModel>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return item is PlayProductUiModel.Product && !item.isPinned
        }

        override fun onBindViewHolder(
            item: PlayProductUiModel.Product,
            holder: ProductFeaturedViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductFeaturedViewHolder {
            return ProductFeaturedViewHolder.create(
                parent,
                listener,
            )
        }
    }

    class PinnedProductDelegate(
        private val listener: ProductCarouselViewHolder.PinnedProduct.Listener,
    ) : BaseAdapterDelegate<PlayProductUiModel.Product, PlayProductUiModel, ProductCarouselViewHolder.PinnedProduct>(
        commonR.layout.view_play_empty
    ) {

        override fun isForViewType(
            itemList: List<PlayProductUiModel>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return item is PlayProductUiModel.Product && item.isPinned
        }

        override fun onBindViewHolder(
            item: PlayProductUiModel.Product,
            holder: ProductCarouselViewHolder.PinnedProduct
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductCarouselViewHolder.PinnedProduct {
            return ProductCarouselViewHolder.PinnedProduct.create(
                parent,
                listener,
            )
        }
    }
}