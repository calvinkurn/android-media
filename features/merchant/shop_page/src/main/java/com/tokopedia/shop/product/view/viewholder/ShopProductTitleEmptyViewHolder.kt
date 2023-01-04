package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopProductTitleEmptyBinding
import com.tokopedia.shop.product.view.datamodel.ShopProductTitleEmptyUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopProductTitleEmptyViewHolder(val view: View) : AbstractViewHolder<ShopProductTitleEmptyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_product_title_empty
    }

    private val viewBinding: ItemShopProductTitleEmptyBinding? by viewBinding()
    private val tvTitle: Typography? = viewBinding?.tvTitle

    override fun bind(element: ShopProductTitleEmptyUiModel?) {
        tvTitle?.text = getString(R.string.shop_product_search_title_empty_state)
    }
}
