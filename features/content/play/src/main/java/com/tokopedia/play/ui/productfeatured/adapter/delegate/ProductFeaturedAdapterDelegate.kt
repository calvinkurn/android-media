package com.tokopedia.play.ui.productfeatured.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedAdapterDelegate(
        private val listener: ProductBasicViewHolder.Listener
) : TypedAdapterDelegate<PlayProductUiModel.Product, PlayProductUiModel, ProductFeaturedViewHolder>(ProductFeaturedViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayProductUiModel.Product, holder: ProductFeaturedViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductFeaturedViewHolder {
        return ProductFeaturedViewHolder(basicView, listener)
    }
}