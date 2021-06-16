package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.cart.domain.model.cartlist.UnavailableGroupData
import com.tokopedia.cart.view.uimodel.*
import javax.inject.Inject

class ViewHolderDataMapper @Inject constructor() {

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

    fun mapDisabledShopHolderData(shopGroupWithErrorData: ShopGroupWithErrorData): DisabledShopHolderData {
        return DisabledShopHolderData(
                shopId = shopGroupWithErrorData.shopId,
                shopName = shopGroupWithErrorData.shopName,
                shopBadgeUrl = shopGroupWithErrorData.shopTypeInfoData.shopBadge,
                isFulfillment = shopGroupWithErrorData.isFulfillment,
                isTokoNow = shopGroupWithErrorData.isTokoNow
        )
    }

    fun mapDisabledItemHolderData(cartItemHolderData: CartItemHolderData, showDivider: Boolean): DisabledCartItemHolderData {
        return DisabledCartItemHolderData(
                cartId = cartItemHolderData.cartItemData?.originData?.cartId ?: 0,
                productId = cartItemHolderData.cartItemData?.originData?.productId ?: "0",
                productImage = cartItemHolderData.cartItemData?.originData?.productImage ?: "",
                productName = cartItemHolderData.cartItemData?.originData?.productName ?: "",
                productPrice = cartItemHolderData.cartItemData?.originData?.pricePlan
                        ?: 0.toDouble(),
                isWishlisted = cartItemHolderData.cartItemData?.originData?.isWishlisted ?: false,
                showDivider = showDivider,
                data = cartItemHolderData.cartItemData,
                actionsData = cartItemHolderData.actionsData,
                selectedUnavailableActionId = cartItemHolderData.cartItemData?.selectedUnavailableActionId ?: 0,
                selectedUnavailableActionLink = cartItemHolderData.cartItemData?.selectedUnavailableActionLink ?: "",
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