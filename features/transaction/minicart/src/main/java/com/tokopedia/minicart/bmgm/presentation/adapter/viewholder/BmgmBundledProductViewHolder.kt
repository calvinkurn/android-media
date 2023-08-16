package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmBundledProductAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmBundledProductItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartBundledProductBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmBundledProductViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.TierUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_bundled_product
    }

    private val binding = ItemBmgmMiniCartBundledProductBinding.bind(itemView)

    override fun bind(element: BmgmMiniCartVisitable.TierUiModel) {

        itemView.setOnClickListener { listener.setOnItemClickedListener() }
        setupProductList(element.products)

        binding.tvBmgmBundledDiscount.text = element.tierDiscountStr
    }

    private fun setupProductList(products: List<BmgmMiniCartVisitable.ProductUiModel>) {
        with(binding.rvBmgmBundledProduct) {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmBundledProductItemDecoration())
            }
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }
            adapter = BmgmBundledProductAdapter(products, listener)
        }
    }
}