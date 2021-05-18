package com.tokopedia.minicart.ui.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.ui.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.ui.cartlist.uimodel.MiniCartShopUiModel

class MiniCartShopViewHolder(private val view: View,
                             private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartShopUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_shop
    }

    override fun bind(element: MiniCartShopUiModel) {

    }

}