package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmBundledProductAdapter
import com.tokopedia.minicart.bmgm.presentation.model.BmgmBundledProductUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmSingleProductUiModel
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartBundledProductBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmBundledProductViewHolder(
    itemView: View
) : AbstractViewHolder<BmgmBundledProductUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_bundled_product
    }

    private val binding = ItemBmgmMiniCartBundledProductBinding.bind(itemView)

    override fun bind(element: BmgmBundledProductUiModel) {

        setupProductList(element.products)
    }

    private fun setupProductList(products: List<BmgmSingleProductUiModel>) {
        with(binding.rvBmgmBundledProduct) {
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }
            adapter = BmgmBundledProductAdapter(products)
        }
    }
}