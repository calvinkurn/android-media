package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeBuyAllBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class BuyAllProductViewHolder(
    itemView: View
) : AbstractViewHolder<BuyAllProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_buy_all
    }

    private var binding: ItemTokopedianowRecipeBuyAllBinding? by viewBinding()

    override fun bind(data: BuyAllProductUiModel) {
        binding?.textPrice?.text = data.totalPrice
    }
}