package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import kotlinx.android.synthetic.main.search_result_shop_card.view.*

internal class ShopItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : AbstractViewHolder<ShopDataView.ShopItem>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_card
    }

    override fun bind(shopDataViewItem: ShopDataView.ShopItem?) {
        if(shopDataViewItem == null) return

        itemView.shopCardView?.setShopCardModel(
                ShopCardModel(
                        id = shopDataViewItem.id,
                        name = shopDataViewItem.name,
                        domain = shopDataViewItem.domain,
                        url = shopDataViewItem.url,
                        applink = shopDataViewItem.applink,
                        image = shopDataViewItem.image,
                        image300 = shopDataViewItem.image300,
                        description = shopDataViewItem.description,
                        tagLine = shopDataViewItem.tagLine,
                        location = shopDataViewItem.location,
                        totalTransaction = shopDataViewItem.totalTransaction,
                        totalFavorite = shopDataViewItem.totalFavorite,
                        goldShop = shopDataViewItem.goldShop,
                        isOwner = shopDataViewItem.isOwner,
                        rateSpeed = shopDataViewItem.rateSpeed,
                        rateAccuracy = shopDataViewItem.rateAccuracy,
                        rateService = shopDataViewItem.rateService,
                        status = shopDataViewItem.status,
                        productList = shopDataViewItem.productList.mapToShopCardProductList(),
                        voucher = shopDataViewItem.voucher.mapToShopCardVoucher(),
                        lucky = shopDataViewItem.lucky,
                        reputationImageUri = shopDataViewItem.reputationImageUri,
                        reputationScore = shopDataViewItem.reputationScore,
                        isOfficial = shopDataViewItem.isOfficial,
                        gaKey = shopDataViewItem.gaKey,
                        isRecommendation = shopDataViewItem.isRecommendation
                ),
                object : ShopCardListener {
                    override fun onItemImpressed() { }

                    override fun onItemClicked() {
                        shopListener.onItemClicked(shopDataViewItem)
                    }

                    override fun onProductItemImpressed(productPreviewIndex: Int) { }

                    override fun onProductItemClicked(productPreviewIndex: Int) {
                        val productItem = shopDataViewItem.productList.getOrNull(productPreviewIndex) ?: return

                        shopListener.onProductItemClicked(productItem)
                    }
                }
        )
    }

    private fun List<ShopDataView.ShopItem.ShopItemProduct>.mapToShopCardProductList() =
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

    private fun ShopDataView.ShopItem.ShopItemVoucher.mapToShopCardVoucher() =
            ShopCardModel.ShopItemVoucher(
                    freeShipping = freeShipping,
                    cashback = ShopCardModel.ShopItemVoucherCashback(
                            cashbackValue = cashback.cashbackValue,
                            isPercentage = cashback.isPercentage
                    )
            )
}