package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemBmgmMiniCartSingleProductBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.loadImage

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
        setupUiConfig(element.ui)
    }

    private fun setupUiConfig(ui: BmgmMiniCartVisitable.ProductUiModel.Ui) {
        with(binding.viewBmsmSingleProduct) {
            val topMargin = itemView.context.dpToPx(ui.topMarginInDp).toInt()
            setPadding(0, topMargin, 0, 0)
        }
    }
}