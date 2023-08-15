package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartSingleProductBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmSingleProductViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.ProductUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_single_product
    }

    private val binding = ItemBmgmMiniCartSingleProductBinding.bind(itemView)

    override fun bind(element: BmgmMiniCartVisitable.ProductUiModel) {
        itemView.setOnClickListener { listener.setOnItemClickedListener() }
        binding.imgBmgmSingleProduct.loadImage(element.productImage)
    }
}