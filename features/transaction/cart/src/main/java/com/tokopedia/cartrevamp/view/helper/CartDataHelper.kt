package com.tokopedia.cartrevamp.view.helper

import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData

object CartDataHelper {

    fun getAllAvailableCartItemData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        val cartItemHolderDataList = data.productUiModelList
                        for (cartItemHolderData in cartItemHolderDataList) {
                            cartItemHolderData.shopBoMetadata = data.boMetadata
                            cartItemHolderData.shopCartShopGroupTickerData =
                                data.cartShopGroupTicker
                            cartItemDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllAvailableCartItemHolderData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.let {
                        cartItemDataList.addAll(it)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllCartItemData(cartDataList: ArrayList<Any>, cartModel: CartModel): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    val cartItemHolderDataList = data.productUiModelList
                    for (cartItemHolderData in cartItemHolderDataList) {
                        cartItemHolderData.shopBoMetadata = data.boMetadata
                        cartItemHolderData.shopCartShopGroupTickerData = data.cartShopGroupTicker
                        cartItemDataList.add(cartItemHolderData)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        if (cartModel.tmpAllUnavailableShop?.isNotEmpty() == true) {
            cartItemDataList.addAll(getCollapsedUnavailableCartItemData(cartModel))
        }

        return cartItemDataList
    }

    private fun getCollapsedUnavailableCartItemData(cartModel: CartModel): List<CartItemHolderData> {
        val cartItemDataList = mutableListOf<CartItemHolderData>()
        cartModel.tmpAllUnavailableShop?.let {
            loop@ for (data in it) {
                when (data) {
                    is CartGroupHolderData -> {
                        cartItemDataList.addAll(data.productUiModelList)
                    }
                }
            }
        }
        return cartItemDataList
    }

    private fun hasReachAllShopItems(data: Any): Boolean {
        return data is CartRecentViewHolderData ||
            data is CartWishlistHolderData ||
            data is CartTopAdsHeadlineData ||
            data is CartRecommendationItemHolderData
    }
}
