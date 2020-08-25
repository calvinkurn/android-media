package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Parcelize
data class CartItemData(
        var originData: OriginData? = null,
        var updatedData: UpdatedData? = null,
        var messageErrorData: MessageErrorData? = null,
        var isSingleChild: Boolean = false,
        var isParentHasErrorOrWarning: Boolean = false,
        var isError: Boolean = false,
        var isWarning: Boolean = false,
        var warningMessageTitle: String? = null,
        var warningMessageDescription: String? = null,
        var errorMessageTitle: String? = null,
        var errorMessageDescription: String? = null,
        var similarProductUrl: String = "",
        var nicotineLiteMessageData: NicotineLiteMessageData? = null,
        var isDisableAllProducts: Boolean = false,
        var isFulfillment: Boolean = false
) : Parcelable {

    @Parcelize
    data class OriginData(
            var cartId: Int = 0,
            var parentId: String? = null,
            var productId: String? = null,
            var productName: String? = null,
            var minOrder: Int = 0,
            var maxOrder: Int = 0,
            var priceChangesState: Int = 0,
            var priceChangesDesc: String? = null,
            var productInvenageByUserInCart: Int = 0,
            var productInvenageByUserLastStockLessThan: Int = 0,
            var productInvenageByUserText: String? = null,
            var pricePlan: Double = 0.toDouble(),
            var pricePlanInt: Int = 0,
            var priceCurrency: Int = 0,
            var priceFormatted: String? = null,
            var wholesalePriceFormatted: String? = null,
            var wholesalePrice: Int = 0,
            var productImage: String? = null,
            var productVarianRemark: String? = null,
            var weightPlan: Double = 0.toDouble(),
            var weightUnit: Int = 0,
            var weightFormatted: String? = null,
            var isPreOrder: Boolean = false,
            var isCod: Boolean = false,
            var isFreeReturn: Boolean = false,
            var isCashBack: Boolean = false,
            var isFavorite: Boolean = false,
            var productCashBack: String? = null,
            var cashBackInfo: String? = null,
            var freeReturnLogo: String? = null,
            var category: String? = null,
            var categoryForAnalytics: String? = null,
            var categoryId: String? = null,
            var wholesalePriceData: List<WholesalePriceData>? = null,
            var trackerAttribution: String? = null,
            var trackerListName: String? = null,
            var originalRemark: String? = null,
            var shopName: String? = null,
            var shopCity: String? = null,
            var shopId: String? = null,
            var shopType: String? = null,
            var isOfficialStore: Boolean = false,
            var isGoldMerchant: Boolean = false,
            var isWishlisted: Boolean = false,
            var originalQty: Int = 0,
            var goldMerchantLogoUrl: String? = null,
            var officialStoreLogoUrl: String? = null,
            var preOrderInfo: String? = null,
            var cartString: String? = null,
            var isCheckboxState: Boolean = false,
            var warehouseId: Int = 0,
            var promoCodes: String? = null,
            var promoDetails: String? = null,
            var priceOriginal: Int = 0,
            var isFreeShipping: Boolean = false,
            var freeShippingBadgeUrl: String? = null,
            var listPromoCheckout: List<String> = emptyList(),
            var variant: String = "",
            var warningMessage: String = "", // eg : sisa 3
            var slashPriceLabel: String = "",
            var initialPriceBeforeDrop: Int = 0,
            var productAlertMessage: String = ""
    ) : Parcelable

    @Parcelize
    data class UpdatedData(
            var quantity: Int = 0,
            var remark: String? = null,
            var maxCharRemark: Int = 0
    ) : Parcelable {

        fun resetQuantity() {
            quantity = 0
        }

        fun decreaseQuantity() {
            if (quantity > 0)
                this.quantity--
        }

        fun increaseQuantity() {
            this.quantity++
        }

    }

    @Parcelize
    data class MessageErrorData(
            var errorCheckoutPriceLimit: String? = null,
            var errorFieldBetween: String? = null,
            var errorFieldMaxChar: String? = null,
            var errorFieldRequired: String? = null,
            var errorProductAvailableStock: String? = null,
            var errorProductAvailableStockDetail: String? = null,
            var errorProductMaxQuantity: String? = null,
            var errorProductMinQuantity: String? = null
    ) : Parcelable

}
