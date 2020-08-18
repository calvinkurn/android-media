package com.tokopedia.cart.view

import com.tokopedia.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.cart.domain.model.cartlist.UnavailableGroupData
import com.tokopedia.cart.view.uimodel.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

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
                shopName = shopGroupWithErrorData.shopName
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
                tickerMessage = cartItemHolderData.cartItemData?.warningMessageTitle,
                similarProduct = cartItemHolderData.cartItemData?.similarProductData,
                nicotineLiteMessageData = cartItemHolderData.cartItemData?.nicotineLiteMessageData,
                showDivider = showDivider,
                data = cartItemHolderData.cartItemData
        )
    }
}