package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItemData(
        var originData: OriginData = OriginData(),
        var updatedData: UpdatedData = UpdatedData(),
        var messageErrorData: MessageErrorData = MessageErrorData(),
        var isSingleChild: Boolean = false,
        var isParentHasErrorOrWarning: Boolean = false,
        var isError: Boolean = false,
        var isWarning: Boolean = false,
        var isDisableAllProducts: Boolean = false,
        var isFulfillment: Boolean = false,
        var selectedUnavailableActionId: Int = 0,
        var selectedUnavailableActionLink: String = "",
        var shouldValidateWeight: Boolean = false
) : Parcelable {

    @Parcelize
    data class OriginData(
            var cartId: Long = 0,
            var parentId: String = "",
            var productId: String = "",
            var productName: String = "",
            var minOrder: Int = 0,
            var maxOrder: Int = 0,
            var priceChangesState: Int = 0,
            var priceChangesDesc: String = "",
            var productInvenageByUserInCart: Int = 0,
            var productInvenageByUserLastStockLessThan: Int = 0,
            var productInvenageByUserText: String = "",
            var pricePlan: Double = 0.toDouble(),
            var pricePlanInt: Long = 0,
            var priceCurrency: Int = 0,
            var priceFormatted: String = "",
            var wholesalePriceFormatted: String? = null,
            var wholesalePrice: Long = 0,
            var productImage: String = "",
            var productVarianRemark: String = "",
            var weightPlan: Double = 0.toDouble(),
            var weightUnit: Int = 0,
            var weightFormatted: String = "",
            var isPreOrder: Boolean = false,
            var isCod: Boolean = false,
            var isFreeReturn: Boolean = false,
            var isCashBack: Boolean = false,
            var isFavorite: Boolean = false,
            var productCashBack: String = "",
            var cashBackInfo: String = "",
            var freeReturnLogo: String = "",
            var category: String = "",
            var categoryForAnalytics: String = "",
            var categoryId: String = "",
            var wholesalePriceData: List<WholesalePriceData> = emptyList(),
            var trackerAttribution: String = "",
            var trackerListName: String = "",
            var originalRemark: String = "",
            var shopName: String = "",
            var shopCity: String = "",
            var shopId: String = "",
            var shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
            var isWishlisted: Boolean = false,
            var originalQty: Int = 0,
            var preOrderInfo: String = "",
            var cartString: String = "",
            var isCheckboxState: Boolean = false,
            var warehouseId: Int = 0,
            var promoCodes: String = "",
            var promoDetails: String = "",
            var priceOriginal: Long = 0,
            var isFreeShippingExtra: Boolean = false,
            var isFreeShipping: Boolean = false,
            var listPromoCheckout: List<String> = emptyList(),
            var variant: String = "",
            var warningMessage: String = "", // eg : sisa 3
            var slashPriceLabel: String = "",
            var initialPriceBeforeDrop: Long = 0,
            var productInformation: List<String> = emptyList(),
            var productAlertMessage: String = "",
            var campaignId: Int = 0,
            var isTokoNow: Boolean = false
    ) : Parcelable

    @Parcelize
    data class UpdatedData(
            var quantity: Int = 0,
            var remark: String = "",
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
            var errorCheckoutPriceLimit: String = "",
            var errorFieldBetween: String = "",
            var errorFieldMaxChar: String = "",
            var errorFieldRequired: String = "",
            var errorProductAvailableStock: String = "",
            var errorProductAvailableStockDetail: String = "",
            var errorProductMaxQuantity: String = "",
            var errorProductMinQuantity: String = ""
    ) : Parcelable

}
