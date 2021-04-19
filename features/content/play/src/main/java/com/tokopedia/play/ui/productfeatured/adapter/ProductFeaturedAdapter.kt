package com.tokopedia.play.ui.productfeatured.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productfeatured.adapter.delegate.ProductFeaturedAdapterDelegate
import com.tokopedia.play.ui.productfeatured.adapter.delegate.ProductFeaturedPlaceholderAdapterDelegate
import com.tokopedia.play.ui.productfeatured.adapter.delegate.ProductFeaturedSeeMoreAdapterDelegate
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedSeeMoreViewHolder
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedAdapter(
        productFeaturedListener: ProductFeaturedViewHolder.Listener,
        productSeeMoreListener: ProductFeaturedSeeMoreViewHolder.Listener
) : BaseDiffUtilAdapter<PlayProductUiModel>() {

    init {
        delegatesManager
                .addDelegate(ProductFeaturedAdapterDelegate(productFeaturedListener))
                .addDelegate(ProductFeaturedSeeMoreAdapterDelegate(productSeeMoreListener))
                .addDelegate(ProductFeaturedPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayProductUiModel, newItem: PlayProductUiModel): Boolean {
        return if (oldItem is PlayProductUiModel.Product && newItem is PlayProductUiModel.Product) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayProductUiModel, newItem: PlayProductUiModel): Boolean {
        return oldItem == newItem
    }
}