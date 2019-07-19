package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import kotlinx.android.synthetic.main.search_result_shop_item_product_card.view.*

class ShopProductItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_item_product_card
    }

    private val context = itemView.context

    fun bind(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct?) {
        if(shopItemProductView == null) return

        initShopItemProductImage(shopItemProductView)
        initShopItemProductPrice(shopItemProductView)
    }

    private fun initShopItemProductImage(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct) {
        if(shopItemProductView.imageUrl == "") return

        itemView.imageViewShopItemProductImage?.let { imageViewShopItemProductImage ->
            ImageHandler.loadImageFitCenter(context, imageViewShopItemProductImage, shopItemProductView.imageUrl)
        }

        itemView.imageViewShopItemProductImage?.setOnClickListener {
            shopListener.onProductItemClicked(shopItemProductView.applink)
        }
    }

    private fun initShopItemProductPrice(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct) {
        val isShopItemProductPriceVisible = getIsShopItemProductPriceVisible(shopItemProductView)

        itemView.textViewShopItemProductPrice.visibility = if (isShopItemProductPriceVisible) View.VISIBLE else View.GONE

        if(isShopItemProductPriceVisible) {
            itemView.textViewShopItemProductPrice.text = MethodChecker.fromHtml(shopItemProductView.priceFormat)
        }
    }

    private fun getIsShopItemProductPriceVisible(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct): Boolean {
        return shopItemProductView.priceFormat != ""
    }
}