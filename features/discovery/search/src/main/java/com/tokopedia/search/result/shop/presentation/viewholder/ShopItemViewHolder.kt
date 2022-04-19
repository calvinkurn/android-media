package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultShopCardBinding
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.utils.view.binding.viewBinding

internal class ShopItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : AbstractViewHolder<ShopDataView.ShopItem>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_card
    }

    private var binding: SearchResultShopCardBinding? by viewBinding()

    override fun bind(shopDataViewItem: ShopDataView.ShopItem?) {
        val binding = binding ?: return
        if(shopDataViewItem == null) return

        binding.shopCardView.setShopCardModel(
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
                        isRecommendation = shopDataViewItem.isRecommendation,
                        isPMPro = shopDataViewItem.isPMPro,
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