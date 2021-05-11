package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlistsimplified.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import javax.inject.Inject

class MiniCartSimplifiedMapper @Inject constructor() {

    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): List<MiniCartSimplifiedData> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartSimplifiedData>()
        miniCartSimplifiedDataList.addAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.addAll(getUnavailableData(miniCartData))
        return miniCartSimplifiedDataList
    }

    private fun getAvailableData(miniCartData: MiniCartData): List<MiniCartSimplifiedData> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartSimplifiedData>()

        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                miniCartSimplifiedDataList.add(
                        MiniCartSimplifiedData().apply {
                            isError = false
                            cartId = cartDetail.cartId
                            productId = cartDetail.product.productId
                            parentProductId = cartDetail.product.parentId
                            quantity = cartDetail.product.productQuantity
                        }
                )
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun getUnavailableData(miniCartData: MiniCartData): List<MiniCartSimplifiedData> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartSimplifiedData>()

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    miniCartSimplifiedDataList.add(
                            MiniCartSimplifiedData().apply {
                                isError = true
                                cartId = cartDetail.cartId
                                productId = cartDetail.product.productId
                                parentProductId = cartDetail.product.parentId
                                quantity = cartDetail.product.productQuantity
                            }
                    )
                }
            }
        }

        return miniCartSimplifiedDataList
    }

}