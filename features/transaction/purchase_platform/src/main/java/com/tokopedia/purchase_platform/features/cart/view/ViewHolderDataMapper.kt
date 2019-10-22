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
        return cartItemHolderData.cartItemData.let {
            DisabledCartItemHolderData(
                    it.originData.cartId,
                    it.originData.productId,
                    it.originData.productImage,
                    it.originData.productName,
                    it.originData.pricePlan,
                    it.errorMessageTitle,
                    it.originData.isWishlisted,
                    it.errorMessageDescription,
                    it.similarProductData,
                    showDivider,
                    it)
        }
    }
}