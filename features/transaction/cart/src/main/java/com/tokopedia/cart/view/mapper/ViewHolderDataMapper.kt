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