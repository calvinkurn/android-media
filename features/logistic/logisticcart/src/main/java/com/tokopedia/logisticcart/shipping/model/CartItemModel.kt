package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItemModel(
        var cartId: Long = 0,
        var shopId: String? = null,
        var shopName: String? = null,
        var productId: Long = 0,
        var name: String? = null,
        var price: Double = 0.0,
        var originalPrice: Double = 0.0,
        var currency: Int = 0,
        var variant: String? = null,
        var weightUnit: Int = 0,
        var weight: Double = 0.0,
        var weightFmt: String? = null,
        var quantity: Int = 0,
        var noteToSeller: String? = null,
        var imageUrl: String? = null,
        var cashback: String? = null,
        var freeReturnLogo: String? = null,
        var preOrderDurationDay: Int = 0,
        var isCashback: Boolean = false,
        var isPreOrder: Boolean = false,
        var isFreeReturn: Boolean = false,
        var fInsurance: Boolean = false,
        var fCancelPartial: Boolean = false,
        var isError: Boolean = false,
        var errorMessage: String? = null,
        var errorMessageDescription: String? = null,
        var isProtectionAvailable: Boolean = false,
        var protectionPricePerProduct: Int = 0,
        var protectionPrice: Int = 0,
        var protectionSubTitle: String? = null,
        var protectionTitle: String? = null,
        var protectionLinkText: String? = null,
        var protectionLinkUrl: String? = null,
        var isProtectionOptIn: Boolean = false,
        var isProtectionCheckboxDisabled: Boolean = false,
        var preOrderInfo: String? = null,
        var isFreeShipping: Boolean = false,
        var isFreeShippingExtra: Boolean = false,
        var isShowTicker: Boolean = false,
        var tickerMessage: String? = null,
        var isWholesalePrice: Boolean = false,
        var analyticsProductCheckoutData: AnalyticsProductCheckoutData = AnalyticsProductCheckoutData(),

        // Trade in
        var isValidTradeIn: Boolean = false,
        var newDevicePrice: Long = 0,
        var oldDevicePrice: Long = 0,
        var deviceModel: String? = null,
        var diagnosticId: String? = null,
        var productInformation: List<String>? = null,
        var productAlertMessage: String? = null

) : Parcelable