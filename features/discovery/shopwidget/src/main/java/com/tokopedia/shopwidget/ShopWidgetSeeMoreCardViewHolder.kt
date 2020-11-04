package com.tokopedia.shopwidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.shop_card_see_more_layout.view.*

class ShopWidgetSeeMoreCardViewHolder(
        itemView: View,
        private val shopWidgetListener: ShopWidgetListener
) : AbstractViewHolder<ShopWidgetModel.ShopCardModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.shop_card_see_more_layout
    }

    override fun bind(element: ShopWidgetModel.ShopCardModel) {
        bindTitle(element)
        bindListener(element)
    }

    private fun bindTitle(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetSeeMoreTitleCard?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.shopWidgetSeeMoreTitleCard?.text = element.title
        }
    }

    private fun bindListener(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetSeeMoreCard?.setOnClickListener {
            shopWidgetListener.onShopSeeMoreClicked(element)
        }
    }
}