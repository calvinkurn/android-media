package com.tokopedia.play.ui.productfeatured.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedSeeMoreViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 24/02/21
 */
class ProductFeaturedSeeMoreAdapterDelegate(
        private val listener: ProductFeaturedSeeMoreViewHolder.Listener
) : TypedAdapterDelegate<PlayProductUiModel.SeeMore, PlayProductUiModel, ProductFeaturedSeeMoreViewHolder>(ProductFeaturedSeeMoreViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayProductUiModel.SeeMore, holder: ProductFeaturedSeeMoreViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductFeaturedSeeMoreViewHolder {
        return ProductFeaturedSeeMoreViewHolder(basicView, listener)
    }
}