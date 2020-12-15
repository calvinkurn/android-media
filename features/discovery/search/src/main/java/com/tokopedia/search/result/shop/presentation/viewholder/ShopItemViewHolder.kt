package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import kotlinx.android.synthetic.main.search_result_shop_card.view.*

internal class ShopItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : AbstractViewHolder<ShopViewModel.ShopItem>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_card
    }

    override fun bind(shopViewItem: ShopViewModel.ShopItem?) {
        if(shopViewItem == null) return

        itemView.shopCardView?.setShopCardModel(
                ShopCardModel(
                        id = shopViewItem.id,
                        name = shopViewItem.name,
                        domain = shopViewItem.domain,
                        url = shopViewItem.url,
                        applink = shopViewItem.applink,
                        image = shopViewItem.image,
                        image300 = shopViewItem.image300,
                        description = shopViewItem.description,
                        tagLine = shopViewItem.tagLine,
                        location = shopViewItem.location,
                        totalTransaction = shopViewItem.totalTransaction,
                        totalFavorite = shopViewItem.totalFavorite,
                        goldShop = shopViewItem.goldShop,
                        isOwner = shopViewItem.isOwner,
                        rateSpeed = shopViewItem.rateSpeed,
                        rateAccuracy = shopViewItem.rateAccuracy,
                        rateService = shopViewItem.rateService,
                        status = shopViewItem.status,
                        productList = shopViewItem.productList.mapToShopCardProductList(),
                        voucher = shopViewItem.voucher.mapToShopCardVoucher(),
                        lucky = shopViewItem.lucky,
                        reputationImageUri = shopViewItem.reputationImageUri,
                        reputationScore = shopViewItem.reputationScore,
                        isOfficial = shopViewItem.isOfficial,
                        gaKey = shopViewItem.gaKey,
                        isRecommendation = shopViewItem.isRecommendation
                ),
                object : ShopCardListener {
                    override fun onItemImpressed() { }

                    override fun onItemClicked() {
                        shopListener.onItemClicked(shopViewItem)
                    }

                    override fun onProductItemImpressed(productPreviewIndex: Int) { }

                    override fun onProductItemClicked(productPreviewIndex: Int) {
                        val productItem = shopViewItem.productList.getOrNull(productPreviewIndex) ?: return

                        shopListener.onProductItemClicked(productItem)
                    }
                }
        )
    }

    private fun List<ShopViewModel.ShopItem.ShopItemProduct>.mapToShopCardProductList() =
            map {
                ShopCardModel.ShopItemProduct(
                        id = it.id,
                        name = it.name,
                        url = it.url,
                        applink = it.applink,
                        price = it.price,
                        priceFormat = it.priceFormat,
                        imageUrl = it.imageUrl,
                        isRecommendation = it.isRecommendation
                )
            }

    private fun ShopViewModel.ShopItem.ShopItemVoucher.mapToShopCardVoucher() =
            ShopCardModel.ShopItemVoucher(
                    freeShipping = freeShipping,
                    cashback = ShopCardModel.ShopItemVoucherCashback(
                            cashbackValue = cashback.cashbackValue,
                            isPercentage = cashback.isPercentage
                    )
            )
}