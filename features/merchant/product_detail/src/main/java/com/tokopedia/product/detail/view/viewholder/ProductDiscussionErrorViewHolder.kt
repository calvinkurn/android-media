package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionErrorDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_local_load.view.*

class ProductDiscussionErrorViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDiscussionErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_discussion_local_load
    }

    override fun bind(element: ProductDiscussionErrorDataModel) {
        itemView.productDetailDiscussionLocalLoad.apply {
            title?.text = getString(R.string.product_detail_discussion_local_load_title)
            description?.text = getString(R.string.product_detail_discussion_local_load_description)
            refreshBtn?.setOnClickListener {
                listener.onDiscussionRefreshClicked()
            }
        }
    }
}