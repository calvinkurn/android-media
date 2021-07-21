package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.cart.domain.model.cartlist.UnavailableGroupData
import com.tokopedia.cart.view.uimodel.*
import javax.inject.Inject

class CartViewHolderDataMapper @Inject constructor() {

    fun mapCartShopHolderData(shopGroupAvailableData: ShopGroupAvailableData, isMultipleShop: Boolean): CartShopHolderData {
        if (shopGroupAvailableData.isTokoNow) {
            return if (isMultipleShop && shopGroupAvailableData.cartItemDataList.size > 1) {
                mapCartShopHolderData(
                        shopGroupAvailableData = shopGroupAvailableData,
                        isCollapsible = true,
                        isCollapsed = true,
                        isShowPin = true
                )
            } else {
                mapCartShopHolderData(
                        shopGroupAvailableData = shopGroupAvailableData,
                        isCollapsible = false,
                        isCollapsed = false,
                        isShowPin = false
                )
            }
        } else {
            return mapCartShopHolderData(
                    shopGroupAvailableData = shopGroupAvailableData,
                    isCollapsible = false,
                    isCollapsed = false,
                    isShowPin = false
            )
        }
    }

    private fun mapCartShopHolderData(shopGroupAvailableData: ShopGroupAvailableData,
                                      isCollapsible: Boolean,
                                      isCollapsed: Boolean,
                                      isShowPin: Boolean): CartShopHolderData {
        return CartShopHolderData().apply {
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
            this.shopGroupAvailableData = shopGroupAvailableData
            this.isCollapsible = isCollapsible
            this.isCollapsed = isCollapsed
            if (isCollapsible) {
                var showMoreWording = ""
                val showLessWording = "Tampilkan Lebih Sedikit"
                val itemCount = shopGroupAvailableData.cartItemDataList.size
                showMoreWording = if (itemCount > 10) {
                    val exceedItemCount = itemCount - 10
                    "+$exceedItemCount lainnya"
                } else {
                    "Lihat selengkapnya"
                }
                this.showMoreWording = showMoreWording
                this.showLessWording = showLessWording
            }
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