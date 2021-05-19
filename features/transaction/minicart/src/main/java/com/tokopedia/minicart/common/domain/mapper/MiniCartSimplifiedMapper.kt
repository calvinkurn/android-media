package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlist.Data
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import javax.inject.Inject

class MiniCartSimplifiedMapper @Inject constructor() {

    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
        return MiniCartSimplifiedData().apply {
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData.data)
            miniCartItems = mapMiniCartListData(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
        }
    }

    private fun mapMiniCartWidgetData(data: Data): MiniCartWidgetData {
        return MiniCartWidgetData().apply {
            totalProductCount = data.totalProductCount
            totalProductError = data.totalProductError
            totalProductPrice = data.totalProductPrice
        }
    }

    private fun mapMiniCartListData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()
        miniCartSimplifiedDataList.addAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.addAll(getUnavailableData(miniCartData))
        return miniCartSimplifiedDataList
    }

    private fun getAvailableData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()

        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                miniCartSimplifiedDataList.add(
                        MiniCartItem().apply {
                            isError = false
                            cartId = cartDetail.cartId
                            productId = cartDetail.product.productId
                            parentProductId = cartDetail.product.parentId
                            quantity = cartDetail.product.productQuantity
                            notes = cartDetail.product.productNotes
                        }
                )
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun getUnavailableData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    miniCartSimplifiedDataList.add(
                            MiniCartItem().apply {
                                isError = true
                                cartId = cartDetail.cartId
                                productId = cartDetail.product.productId
                                parentProductId = cartDetail.product.parentId
                                quantity = cartDetail.product.productQuantity
                                notes = cartDetail.product.productNotes
                            }
                    )
                }
            }
        }

        return miniCartSimplifiedDataList
    }

}