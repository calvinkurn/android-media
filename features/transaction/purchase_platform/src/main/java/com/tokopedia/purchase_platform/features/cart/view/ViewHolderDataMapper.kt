package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledCartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledItemHeaderHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledShopHolderData
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