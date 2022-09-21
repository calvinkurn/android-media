package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.LoadingDataModel

class LoadingViewHolder(view: View) : ProductDetailPageViewHolder<LoadingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_loading
    }

    override fun bind(element: LoadingDataModel) {
    }
}
