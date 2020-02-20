package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 18/01/18.
 */

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
        var similarProductData: SimilarProductData? = null,
        var nicotineLiteMessageData: NicotineLiteMessageData? = null,
        var isDisableAllProducts: Boolean = false,
        var isFulfillment: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(OriginData::class.java.classLoader),
            parcel.readParcelable(UpdatedData::class.java.classLoader),
            parcel.readParcelable(MessageErrorData::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(SimilarProductData::class.java.classLoader),
            parcel.readParcelable(NicotineLiteMessageData::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(originData, flags)
        parcel.writeParcelable(updatedData, flags)
        parcel.writeParcelable(messageErrorData, flags)
        parcel.writeByte(if (isSingleChild) 1 else 0)
        parcel.writeByte(if (isParentHasErrorOrWarning) 1 else 0)
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeByte(if (isWarning) 1 else 0)
        parcel.writeString(warningMessageTitle)
        parcel.writeString(warningMessageDescription)
        parcel.writeString(errorMessageTitle)
        parcel.writeString(errorMessageDescription)
        parcel.writeParcelable(similarProductData, flags)
        parcel.writeParcelable(nicotineLiteMessageData, flags)
        parcel.writeByte(if (isDisableAllProducts) 1 else 0)
        parcel.writeByte(if (isFulfillment) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemData> {
        override fun createFromParcel(parcel: Parcel): CartItemData {
            return CartItemData(parcel)
        }

        override fun newArray(size: Int): Array<CartItemData?> {
            return arrayOfNulls(size)
        }
    }

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
            var freeShippingBadgeUrl: String? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readDouble(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readDouble(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createTypedArrayList(WholesalePriceData),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(cartId)
            parcel.writeString(parentId)
            parcel.writeString(productId)
            parcel.writeString(productName)
            parcel.writeInt(minOrder)
            parcel.writeInt(maxOrder)
            parcel.writeInt(priceChangesState)
            parcel.writeString(priceChangesDesc)
            parcel.writeInt(productInvenageByUserInCart)
            parcel.writeInt(productInvenageByUserLastStockLessThan)
            parcel.writeString(productInvenageByUserText)
            parcel.writeDouble(pricePlan)
            parcel.writeInt(pricePlanInt)
            parcel.writeInt(priceCurrency)
            parcel.writeString(priceFormatted)
            parcel.writeString(wholesalePriceFormatted)
            parcel.writeString(productImage)
            parcel.writeString(productVarianRemark)
            parcel.writeDouble(weightPlan)
            parcel.writeInt(weightUnit)
            parcel.writeString(weightFormatted)
            parcel.writeByte(if (isPreOrder) 1 else 0)
            parcel.writeByte(if (isCod) 1 else 0)
            parcel.writeByte(if (isFreeReturn) 1 else 0)
            parcel.writeByte(if (isCashBack) 1 else 0)
            parcel.writeByte(if (isFavorite) 1 else 0)
            parcel.writeString(productCashBack)
            parcel.writeString(cashBackInfo)
            parcel.writeString(freeReturnLogo)
            parcel.writeString(category)
            parcel.writeString(categoryForAnalytics)
            parcel.writeString(categoryId)
            parcel.writeTypedList(wholesalePriceData)
            parcel.writeString(trackerAttribution)
            parcel.writeString(trackerListName)
            parcel.writeString(originalRemark)
            parcel.writeString(shopName)
            parcel.writeString(shopCity)
            parcel.writeString(shopId)
            parcel.writeString(shopType)
            parcel.writeByte(if (isOfficialStore) 1 else 0)
            parcel.writeByte(if (isGoldMerchant) 1 else 0)
            parcel.writeByte(if (isWishlisted) 1 else 0)
            parcel.writeInt(originalQty)
            parcel.writeString(goldMerchantLogoUrl)
            parcel.writeString(officialStoreLogoUrl)
            parcel.writeString(preOrderInfo)
            parcel.writeString(cartString)
            parcel.writeByte(if (isCheckboxState) 1 else 0)
            parcel.writeInt(warehouseId)
            parcel.writeString(promoCodes)
            parcel.writeString(promoDetails)
            parcel.writeInt(priceOriginal)
            parcel.writeByte(if (isFreeShipping) 1 else 0)
            parcel.writeString(freeShippingBadgeUrl)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<OriginData> {
            override fun createFromParcel(parcel: Parcel): OriginData {
                return OriginData(parcel)
            }

            override fun newArray(size: Int): Array<OriginData?> {
                return arrayOfNulls(size)
            }
        }

    }

    data class UpdatedData(
            var quantity: Int = 0,
            var remark: String? = null,
            var maxCharRemark: Int = 0
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt()) {
        }

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

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(quantity)
            parcel.writeString(remark)
            parcel.writeInt(maxCharRemark)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UpdatedData> {
            override fun createFromParcel(parcel: Parcel): UpdatedData {
                return UpdatedData(parcel)
            }

            override fun newArray(size: Int): Array<UpdatedData?> {
                return arrayOfNulls(size)
            }
        }

    }

    data class MessageErrorData(
            var errorCheckoutPriceLimit: String? = null,
            var errorFieldBetween: String? = null,
            var errorFieldMaxChar: String? = null,
            var errorFieldRequired: String? = null,
            var errorProductAvailableStock: String? = null,
            var errorProductAvailableStockDetail: String? = null,
            var errorProductMaxQuantity: String? = null,
            var errorProductMinQuantity: String? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(errorCheckoutPriceLimit)
            parcel.writeString(errorFieldBetween)
            parcel.writeString(errorFieldMaxChar)
            parcel.writeString(errorFieldRequired)
            parcel.writeString(errorProductAvailableStock)
            parcel.writeString(errorProductAvailableStockDetail)
            parcel.writeString(errorProductMaxQuantity)
            parcel.writeString(errorProductMinQuantity)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MessageErrorData> {
            override fun createFromParcel(parcel: Parcel): MessageErrorData {
                return MessageErrorData(parcel)
            }

            override fun newArray(size: Int): Array<MessageErrorData?> {
                return arrayOfNulls(size)
            }
        }
    }

}
