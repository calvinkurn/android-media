package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
        var isError: Boolean = false,
        var errorMessage: String = "",
        var errorMessageDescription: String = "",
        var cartId: Long = 0,
        var productId: Long = 0,
        var productName: String = "",
        var productPriceFmt: String = "",
        var productPrice: Long = 0,
        var productOriginalPrice: Long = 0,
        var productWholesalePrice: Long = 0,
        var productWholesalePriceFmt: String = "",
        var productWeightFmt: String = "",
        var productWeight: Int = 0,
        var productWeightActual: Int = 0,
        var productCondition: Int = 0,
        var productUrl: String = "",
        var isProductReturnable: Boolean = false,
        var isProductIsFreeReturns: Boolean = false,
        var isProductIsPreorder: Boolean = false,
        var preOrderDurationDay: Int = 0,
        var productCashback: String = "",
        var productMinOrder: Int = 0,
        var productInvenageValue: Int = 0,
        var productSwitchInvenage: Int = 0,
        var productPriceCurrency: Int = 0,
        var productImageSrc200Square: String = "",
        var productNotes: String = "",
        var productQuantity: Int = 0,
        var isProductFinsurance: Boolean = false,
        var isProductFcancelPartial: Boolean = false,
        var productCatId: Int = 0,
        var purchaseProtectionPlanData: PurchaseProtectionPlanData = PurchaseProtectionPlanData(),
        var productPreOrderInfo: String = "",
        var tradeInInfoData: TradeInInfoData = TradeInInfoData(),
        var isFreeShipping: Boolean = false,
        var isFreeShippingExtra: Boolean = false,
        var isShowTicker: Boolean = false,
        var tickerMessage: String = "",
        var variant: String = "",
        var productAlertMessage: String = "",
        var productInformation: List<String> = emptyList(),
        var analyticsProductCheckoutData: AnalyticsProductCheckoutData = AnalyticsProductCheckoutData()
) : Parcelable