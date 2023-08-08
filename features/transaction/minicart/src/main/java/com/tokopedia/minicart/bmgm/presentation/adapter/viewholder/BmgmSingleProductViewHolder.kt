package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartSingleProductBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmSingleProductViewHolder(
    itemView: View
) : AbstractViewHolder<BmgmCommonDataUiModel.SingleProductUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_single_product
    }

    private val binding = ItemBmgmMiniCartSingleProductBinding.bind(itemView)

    override fun bind(element: BmgmCommonDataUiModel.SingleProductUiModel) {
        binding.imgBmgmSingleProduct.loadImage(element.productImage)
    }
}