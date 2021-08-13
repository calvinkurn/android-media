package com.tokopedia.play.ui.productfeatured.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedPlaceholderViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 24/02/21
 */
class ProductFeaturedPlaceholderAdapterDelegate : TypedAdapterDelegate<PlayProductUiModel.Placeholder, PlayProductUiModel, ProductFeaturedPlaceholderViewHolder>(ProductFeaturedPlaceholderViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayProductUiModel.Placeholder, holder: ProductFeaturedPlaceholderViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductFeaturedPlaceholderViewHolder {
        return ProductFeaturedPlaceholderViewHolder(basicView)
    }
}