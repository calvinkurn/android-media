package com.tokopedia.topads.dashboard.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductEmptyModel
import kotlinx.android.synthetic.main.topads_dash_no_search_result.view.*

/**
 * Created by Pika on 7/6/20.
 */
class ProductEmptyViewHolder(val view: View) : ProductViewHolder<ProductEmptyModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_no_search_result
    }

    override fun bind(item: ProductEmptyModel, selectMode: Boolean, statsData: MutableList<WithoutGroupDataItem>) {
        item.let {
            view.text_title.text = view.context.getString(R.string.topads_dash_no_product_found)
            view.image_empty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
        }
    }

}