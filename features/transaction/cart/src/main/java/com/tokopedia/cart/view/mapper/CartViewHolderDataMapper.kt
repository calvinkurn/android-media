package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.cart.domain.model.cartlist.UnavailableGroupData
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.cart.view.uimodel.now.CartCollapsedProductHolderData
import com.tokopedia.cart.view.uimodel.now.CartCollapsedProductListHolderData
import com.tokopedia.cart.view.uimodel.now.CartShopSimpleHolderData
import javax.inject.Inject

class CartViewHolderDataMapper @Inject constructor() {

    fun mapCartShopSimpleHolderData(shopGroupAvailableData: ShopGroupAvailableData): CartShopSimpleHolderData {
        return CartShopSimpleHolderData().apply {
            isTokoNow = shopGroupAvailableData.isTokoNow
            isChecked = shopGroupAvailableData.isChecked
            shopName = shopGroupAvailableData.shopName
            shopBadgeUrl = shopGroupAvailableData.shopTypeInfo.shopBadge
            imageFulfilmentUrl = shopGroupAvailableData.fulfillmentBadgeUrl
            shopLocation = shopGroupAvailableData.fulfillmentName
            estimatedTimeArrival = shopGroupAvailableData.estimatedTimeArrival
            preOrderInfo = shopGroupAvailableData.preOrderInfo
            freeShippingUrl = shopGroupAvailableData.freeShippingBadgeUrl
            incidentInfo = shopGroupAvailableData.incidentInfo
        }
    }

    fun mapCartCollapsedProductListHolderData(shopGroupAvailableData: ShopGroupAvailableData): CartCollapsedProductListHolderData {
        val tmpCartCollapsedProductHolderDataList = mutableListOf<CartCollapsedProductHolderData>()
        shopGroupAvailableData.cartItemDataList.forEach {
            val data = mapCartCollapsedProductHolderData(it)
            tmpCartCollapsedProductHolderDataList.add(data)
        }
        return CartCollapsedProductListHolderData().apply {
            cartCollapsedProductHolderDataList = tmpCartCollapsedProductHolderDataList
        }
    }

    fun mapCartShopHolderData(shopGroupAvailableData: ShopGroupAvailableData): CartShopHolderData {
        return CartShopHolderData().apply {
            if (shopGroupAvailableData.isError) {
                setAllItemSelected(false)
            } else {
                if (shopGroupAvailableData.isChecked) {
                    setAllItemSelected(true)
                } else if (shopGroupAvailableData.cartItemDataList.size > 1) {
                    shopGroupAvailableData.cartItemDataList.let {
                        for (cartItemHolderData in it) {
                            if (cartItemHolderData.isSelected) {
                                isPartialSelected = true
                                break
                            }
                        }
                    }
                }
            }
            this.shopGroupAvailableData = shopGroupAvailableData
        }
    }

    private fun mapCartCollapsedProductHolderData(cartItemHolderData: CartItemHolderData): CartCollapsedProductHolderData {
        return CartCollapsedProductHolderData().apply {
            productId = cartItemHolderData.cartItemData.originData.productId
            productImageUrl = cartItemHolderData.cartItemData.originData.productImage
            productName = cartItemHolderData.cartItemData.originData.productName
            productPrice = cartItemHolderData.cartItemData.originData.pricePlanInt
            productQuantity = cartItemHolderData.cartItemData.updatedData.quantity
            productVariantName = cartItemHolderData.cartItemData.originData.variant
        }
    }

    fun mapDisabledItemHeaderHolderData(disabledProductCount: Int): DisabledItemHeaderHolderData {
        return DisabledItemHeaderHolderData(
                disabledItemCount = disabledProductCount
        )
    }

    fun mapDisabledReasonHolderData(unavailableGroupData: UnavailableGroupData): DisabledReasonHolderData {
        return DisabledReasonHolderData().apply {
            title = unavailableGroupData.title
            subTitle = unavailableGroupData.description
        }
    }

    fun mapDisabledShopHolderData(shopGroupWithErrorData: ShopGroupWithErrorData, reason: String): DisabledShopHolderData {
        return DisabledShopHolderData(
                shopId = shopGroupWithErrorData.shopId,
                shopName = shopGroupWithErrorData.shopName,
                shopBadgeUrl = shopGroupWithErrorData.shopTypeInfoData.shopBadge,
                isFulfillment = shopGroupWithErrorData.isFulfillment,
                isTokoNow = shopGroupWithErrorData.isTokoNow,
                reason = reason
        )
    }

    fun mapDisabledItemHolderData(cartItemHolderData: CartItemHolderData, showDivider: Boolean): DisabledCartItemHolderData {
        return DisabledCartItemHolderData(
                cartId = cartItemHolderData.cartItemData.originData.cartId,
                productId = cartItemHolderData.cartItemData.originData.productId,
                productImage = cartItemHolderData.cartItemData.originData.productImage,
                productName = cartItemHolderData.cartItemData.originData.productName,
                productPrice = cartItemHolderData.cartItemData.originData.pricePlan,
                isWishlisted = cartItemHolderData.cartItemData.originData.isWishlisted,
                showDivider = showDivider,
                data = cartItemHolderData.cartItemData,
                actionsData = cartItemHolderData.actionsData,
                selectedUnavailableActionId = cartItemHolderData.cartItemData.selectedUnavailableActionId,
                selectedUnavailableActionLink = cartItemHolderData.cartItemData.selectedUnavailableActionLink,
                errorType = cartItemHolderData.errorType
        )
    }

    fun mapDisabledAccordionHolderData(cartListData: CartListData): DisabledAccordionHolderData {
        return DisabledAccordionHolderData(
                isCollapsed = true,
                showMoreWording = cartListData.showMoreUnavailableDataWording,
                showLessWording = cartListData.showLessUnavailableDataWording
        )
    }
}