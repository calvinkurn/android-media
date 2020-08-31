package com.tokopedia.cart.view

import com.tokopedia.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cart.view.uimodel.DisabledShopHolderData
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

    fun mapDisabledShopHolderData(shopGroupWithErrorData: ShopGroupWithErrorData): DisabledShopHolderData {
        return DisabledShopHolderData(
                shopId = shopGroupWithErrorData.shopId,
                shopName = shopGroupWithErrorData.shopName,
                shopLocation = shopGroupWithErrorData.cityName,
                errorLabel = shopGroupWithErrorData.errorLabel
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
                error = cartItemHolderData.cartItemData?.errorMessageTitle,
                isWishlisted = cartItemHolderData.cartItemData?.originData?.isWishlisted ?: false,
                tickerMessage = cartItemHolderData.cartItemData?.warningMessageTitle,
                similarProduct = cartItemHolderData.cartItemData?.similarProductData,
                nicotineLiteMessageData = cartItemHolderData.cartItemData?.nicotineLiteMessageData,
                showDivider = showDivider,
                data = cartItemHolderData.cartItemData
        )
    }
}