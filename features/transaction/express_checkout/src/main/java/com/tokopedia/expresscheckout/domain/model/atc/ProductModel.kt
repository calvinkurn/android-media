package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductModel(
        var errors: ArrayList<String>? = null,
        var productId: Int = 0,
        var cartId: Long = 0,
        var productName: String? = null,
        var productPriceFmt: String? = null,
        var productPrice: Int = 0,
        var productWholesalePrice: Int = 0,
        var productWholesalePriceFmt: String? = null,
        var productWeightFmt: String? = null,
        var productWeight: Int = 0,
        var productCondition: Int = 0,
        var productUrl: String? = null,
        var productReturnable: Int = 0,
        var productIsFreeReturns: Int = 0,
        var productIsPreorder: Int = 0,
        var productCashback: String? = null,
        var productMinOrder: Int = 0,
        var productInvenageValue: Int = 0,
        var productSwitchInvenage: Int = 0,
        var productPriceCurrency: Int = 0,
        var productImageSrc200Square: String? = null,
        var productNotes: String? = null,
        var productQuantity: Int = 0,
        var productMenuId: Int = 0,
        var productFinsurance: Int = 0,
        var productFcancelPartial: Int = 0,
        var productShipmentModels: ArrayList<ProductShipmentModel>? = null,
        var productShipmentMappingModels: ArrayList<ProductShipmentMappingModel>? = null,
        var productCatId: Int = 0,
        var productCatalogId: Int = 0,
        var purchaseProtectionPlanDataModel: PurchaseProtectionPlanDataModel? = null,
        var freeReturnsModel: FreeReturnsModel? = null,
        var productCategory: String? = null,
        var productTrackerDataModel: ProductTrackerDataModel? = null,
        var productPreorderModel: ProductPreorderModel? = null,
        var wholesalePriceModel: ArrayList<WholesalePriceModel>? = null,
        var productVariantDataModels: ArrayList<ProductVariantDataModel>? = null
)