package com.tokopedia.shopwidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.shop_card_layout.view.*

class ShopWidgetCardViewHolder(
        itemView: View,
        private val shopWidgetListener: ShopWidgetListener
) : AbstractViewHolder<ShopWidgetModel.ShopCardModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.shop_card_layout
    }

    override fun bind(element: ShopWidgetModel.ShopCardModel) {
        bindImage(element)
        bindIconTitle(element)
        bindTitle(element)
        bindSubtitle(element)
        bindProductImage(element)
        bindListener(element)
    }

    private fun bindImage(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetCardImage?.let {
            ImageHandler.loadImageCircle2(itemView.context, it, element.imageUrl)
        }
    }

    private fun bindIconTitle(item: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetCardIconTitle?.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(itemView.shopWidgetCardIconTitle, item.iconTitle)
        }
    }

    private fun bindTitle(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.shopWidgetCardTitle?.text = element.title
        }
    }

    private fun bindSubtitle(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetCardSubtitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.shopWidgetCardSubtitle?.text = element.subtitle
        }
    }

    private fun bindProductImage(element: ShopWidgetModel.ShopCardModel) {
        if (element.products.isNotEmpty()) {
            bindProductImage1(element.products[0].imageUrl)
            if (element.products.size >= 2) bindProductImage2(element.products[1].imageUrl)
            if (element.products.size >= 3) bindProductImage3(element.products[2].imageUrl)
        }
    }

    private fun bindProductImage1(imageUrl: String) {
        itemView.shopWidgetCardProduct1?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage2(imageUrl: String) {
        itemView.shopWidgetCardProduct2?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage3(imageUrl: String) {
        itemView.shopWidgetCardProduct3?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindListener(element: ShopWidgetModel.ShopCardModel) {
        itemView.shopWidgetCard?.setOnClickListener {
            shopWidgetListener.onShopCardClicked(element)
        }
    }
}