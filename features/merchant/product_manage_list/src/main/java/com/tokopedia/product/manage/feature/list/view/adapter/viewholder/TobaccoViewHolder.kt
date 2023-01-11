package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.databinding.ItemManageProductListTobaccoBinding
import com.tokopedia.utils.view.binding.viewBinding

class TobaccoViewHolder(
    view: View
) : AbstractViewHolder<ProductUiModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list_tobacco
    }

    private val binding by viewBinding<ItemManageProductListTobaccoBinding>()

    override fun bind(product: ProductUiModel) {
    }
}
