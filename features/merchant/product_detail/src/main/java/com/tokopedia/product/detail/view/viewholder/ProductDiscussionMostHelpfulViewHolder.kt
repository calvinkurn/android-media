package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductDiscussionMostHelpfulViewHolder(view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    override fun bind(element: ProductDiscussionMostHelpfulDataModel) {
        TODO("Not yet implemented")
    }
}