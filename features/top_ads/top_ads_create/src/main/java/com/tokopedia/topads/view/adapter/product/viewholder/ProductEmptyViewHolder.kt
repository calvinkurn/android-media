package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyUiModel

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductEmptyViewHolder(private val mView: View): ProductViewHolder<ProductEmptyUiModel>(mView) {

    private val emptyStateProductList : GlobalError? = mView.findViewById(R.id.emptyStateProductList)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyUiModel) {
        emptyStateProductList?.apply{
            errorAction.hide()
            errorTitle.text = mView.context.getString(R.string.topads_ads_empty_view_title)
            errorDescription.text = mView.context.getString(R.string.topads_ads_empty_view_description)
        }
    }

}
