package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmTierProduct
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var isError: Boolean = false,
    var errorMessage: String = "",
    var errorMessageDescription: String = "",
    var cartId: Long = 0,
    var productId: Long = 0,
    var productName: String = "",
    var productPrice: Double = 0.0,
    var productOriginalPrice: Double = 0.0,
    var productWholesalePrice: Double = 0.0,
    var productWeightFmt: String = "",
    var productWeight: Int = 0,
    var productWeightActual: Int = 0,
    var isProductIsFreeReturns: Boolean = false,
    var isProductIsPreorder: Boolean = false,
    var preOrderDurationDay: Int = 0,
    var productCashback: String = "",
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
    var freeShippingName: String = "",
    var isShowTicker: Boolean = false,
    var tickerMessage: String = "",
    var variant: String = "",
    var variantParentId: String = "",
    var productAlertMessage: String = "",
    var productInformation: List<String> = emptyList(),
    var analyticsProductCheckoutData: AnalyticsProductCheckoutData = AnalyticsProductCheckoutData(),
    var isBundlingItem: Boolean = false,
    var bundlingItemPosition: Int = 0,
    var bundleId: String = "",
    var bundleGroupId: String = "",
    var bundleType: String = "",
    var bundleTitle: String = "",
    var bundlePrice: Double = 0.0,
    var bundleSlashPriceLabel: String = "",
    var bundleOriginalPrice: Double = 0.0,
    var bundleQuantity: Int = 0,
    var bundleIconUrl: String = "",
    var addOnGiftingProduct: AddOnGiftingDataModel = AddOnGiftingDataModel(),
    var ethicalDrugs: EthicalDrugDataModel = EthicalDrugDataModel(),
    var addOnProduct: AddOnProductDataModel = AddOnProductDataModel(),

    // bmgm
    var isBmgmItem: Boolean = false,
    var bmgmIconUrl: String = "",
    var bmgmOfferId: Long = 0,
    var bmgmOfferName: String = "",
    var bmgmOfferMessage: List<String> = emptyList(),
    var bmgmOfferStatus: Int = 0,
    var bmgmItemPosition: Int = 0,
    var bmgmTotalDiscount: Double = 0.0,
    var bmgmTierProductList: List<BmGmTierProduct> = emptyList(),

    // new
    val shouldShowShopInfo: Boolean = false,
    val shopName: String = "",
    val shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    val originWarehouseIds: List<Long> = emptyList(),
    var campaignId: Int = -1
) : Parcelable
