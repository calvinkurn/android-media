package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemMiniCartGwpGiftListBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartGiftListAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.itemdecoration.GwpMiniCartGiftItemDecoration
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class MiniCartGwpGiftWidgetViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_mini_cart_gwp_gift_list
    }

    private val binding by lazy { ItemMiniCartGwpGiftListBinding.bind(itemView) }
    private val mAdapter by lazy { GwpMiniCartGiftListAdapter(listener) }

    override fun bind(element: BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
        setupRecyclerView()
        showGifts(element.productList)
        showRibbon(element)

        itemView.setOnClickListener {
            listener.setOnItemClickedListener()
        }
    }

    private fun showRibbon(element: BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
        binding.miniCartRibbonView.setText(element.benefitWording)
    }

    private fun setupRecyclerView() {
        with(binding.rvMiniCartGiftList) {
            if (layoutManager == null) {
                layoutManager = createLayoutManager()
            }
            adapter = mAdapter
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(GwpMiniCartGiftItemDecoration())
            }
        }
    }

    private fun showGifts(products: List<ProductGiftUiModel>) {
        mAdapter.submitList(products)
    }

    private fun createLayoutManager(): LinearLayoutManager {
        return object : LinearLayoutManager(
            itemView.context.applicationContext, HORIZONTAL, false
        ) {
            override fun canScrollHorizontally(): Boolean = false
        }
    }
}