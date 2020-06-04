package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductLoadingViewHolder

/**
 * Created by jegul on 03/06/20
 */
class ProductLoadingAdapterDelegate : TypedAdapterDelegate<ProductLoadingUiModel, ProductUiModel, ProductLoadingViewHolder>(ProductLoadingViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductLoadingUiModel, holder: ProductLoadingViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductLoadingViewHolder {
        return ProductLoadingViewHolder(basicView)
    }
}