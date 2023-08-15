package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmProductPlaceholderViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.PlaceholderUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_product_placeholder
    }

    override fun bind(element: BmgmMiniCartVisitable.PlaceholderUiModel) {
        itemView.setOnClickListener { listener.setOnItemClickedListener() }
    }
}