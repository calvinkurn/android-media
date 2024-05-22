package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.domain.model.bmgm.CheckoutBmgmTierProductModel
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel

data class CheckoutProductModel(
    override val cartStringGroup: String,

    // product data
    val cartId: Long = 0,
    val productId: Long = 0,
    val productCatId: Long = 0,
    val lastLevelCategory: String = "",
    val categoryIdentifier: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val campaignId: Int = 0,
    val variant: String = "",
    val variantParentId: String = "",
    val weightUnit: Int = 0,
    val weight: Double = 0.0,
    val weightFmt: String = "",
    val weightActual: Double = 0.0,
    val quantity: Int = 0,
    val prevQuantity: Int = 0,
    val noteToSeller: String = "",
    val shouldShowLottieNotes: Boolean = false,
    val imageUrl: String = "",
    val cashback: String = "",
    val preOrderDurationDay: Int = 0,
    val isCashback: Boolean = false,
    val isPreOrder: Boolean = false,
    val isFreeReturn: Boolean = false,
    val fInsurance: Boolean = false,
    val fCancelPartial: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String = "",
    val errorMessageDescription: String = "",
    val isProtectionAvailable: Boolean = false,
    val protectionPricePerProduct: Int = 0,
    val protectionPrice: Double = 0.0,
    val protectionSubTitle: String = "",
    val protectionTitle: String = "",
    val protectionLinkText: String = "",
    val protectionLinkUrl: String = "",
    val isProtectionOptIn: Boolean = false,
    val isProtectionCheckboxDisabled: Boolean = false,
    val preOrderInfo: String = "",
    val isFreeShipping: Boolean = false,
    val isFreeShippingExtra: Boolean = false,
    val freeShippingName: String = "",
    val isShowTicker: Boolean = false,
    val tickerMessage: String = "",
    val isWholesalePrice: Boolean = false,
    val analyticsProductCheckoutData: AnalyticsProductCheckoutData = AnalyticsProductCheckoutData(),
    val isValidTradeIn: Boolean = false,
    val newDevicePrice: Long = 0,
    val oldDevicePrice: Long = 0,
    val deviceModel: String = "",
    val diagnosticId: String = "",
    val productInformation: List<String> = emptyList(),
    val productAlertMessage: String = "",
    val isBundlingItem: Boolean = false,
    val bundlingItemPosition: Int = 0,
    val bundleId: String = "",
    val bundleGroupId: String = "",
    val bundleType: String = "",
    val bundleTitle: String = "",
    val bundlePrice: Double = 0.0,
    val bundleSlashPriceLabel: String = "",
    val bundleOriginalPrice: Double = 0.0,
    val bundleQuantity: Int = 0,
    val bundleIconUrl: String = "",
    val addOnGiftingProductLevelModel: AddOnGiftingDataModel = AddOnGiftingDataModel(),
    val addOnDefaultFrom: String = "",
    val addOnDefaultTo: String = "",
    val ethicalDrugDataModel: EthicalDrugDataModel = EthicalDrugDataModel(),
    val cartItemPosition: Int = 0,
    var isLastItemInOrder: Boolean = false,
    val shouldShowShopInfo: Boolean = false,
    val originWarehouseIds: List<Long> = emptyList(),
    val addOnProduct: AddOnProductDataModel = AddOnProductDataModel(),
    val addOnGiftingWording: AddOnGiftingWordingModel = AddOnGiftingWordingModel(),

    // bmgm data
    val isBMGMItem: Boolean = false,
    val bmgmOfferId: Long = 0,
    val bmgmOfferTypeId: Long = 0,
    val bmgmOfferName: String = "",
    val bmgmOfferMessage: List<String> = emptyList(),
    val bmgmOfferStatus: Int = 0,
    val bmgmIconUrl: String = "",
    val bmgmTotalDiscount: Double = 0.0,
    val bmgmItemPosition: Int = 0,
    val bmgmTierProductList: List<CheckoutBmgmTierProductModel> = emptyList(),
    val shouldShowBmgmInfo: Boolean = false,

    // shop data
    val shopId: String = "",
    val shopName: String = "",
    var isShopError: Boolean = false,
    val isTokoCabang: Boolean = false,
    val warehouseId: String = "",
    val shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    val cartStringOrder: String = "",
    val shopTier: Int = 0,

    // group data
    val uiGroupType: Int = 0,
    val groupInfoName: String = "",
    val groupInfoBadgeUrl: String = "",
    val groupInfoDescription: String = "",
    val groupInfoDescriptionBadgeUrl: String = "",
    val orderNumber: Int = 0,
    val groupPreOrderInfo: String = "",
    val freeShippingBadgeUrl: String = "",
    val isFreeShippingPlus: Boolean = false, // flag for plus badge tracker
    var hasSeenFreeShippingBadge: Boolean = false, // flag for tracker
    val shopLocation: String = "",
    val shouldShowGroupInfo: Boolean = false,

    // additional feature
    val enableNoteEdit: Boolean = false,
    val enableQtyEdit: Boolean = false,

    val shouldShowMinQtyError: Boolean = false,
    val shouldShowMaxQtyError: Boolean = false,
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val invenageValue: Int = 0,
    val switchInvenage: Int = 0,
    val isCartTypeOcc: Boolean = true
) : CheckoutItem {

    val shouldShowBmgmInfoIcon: Boolean
        get() {
            return shouldShowBmgmInfo && bmgmOfferTypeId != OFFER_TYPE_ID_GWP
        }

    companion object {
        private const val OFFER_TYPE_ID_GWP: Long = 2L
    }
}
