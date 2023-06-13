package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel

class TobaccoViewHolder(
    view: View
) : AbstractViewHolder<ProductUiModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list_tobacco
    }

    override fun bind(product: ProductUiModel) {
    }
}
