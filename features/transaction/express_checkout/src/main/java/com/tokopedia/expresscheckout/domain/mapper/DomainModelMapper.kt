package com.tokopedia.expresscheckout.domain.mapper

import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutResponse
import com.tokopedia.expresscheckout.domain.model.*
import com.tokopedia.transactiondata.entity.response.shippingaddressform.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.ProductVariantData
import com.tokopedia.transactiondata.entity.response.variantdata.Variant

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class DomainModelMapper : DataMapper {

    override fun convertToDomainModel(expressCheckoutResponse: ExpressCheckoutResponse): AtcExpressCheckoutModel {
        var responseModel = AtcExpressCheckoutModel()
        responseModel.status = expressCheckoutResponse.status
        responseModel.headerModel = getHeaderModel(expressCheckoutResponse)
        responseModel.dataModel = getDataModel(expressCheckoutResponse)
        return responseModel
    }

    private fun getDataModel(expressCheckoutResponse: ExpressCheckoutResponse): DataModel {
        var dataModel = DataModel()
        dataModel.errorCode = expressCheckoutResponse.data.errorCode
        dataModel.errors = expressCheckoutResponse.data.errors
        dataModel.success = expressCheckoutResponse.data.success

        var cartModel = CartModel()
        cartModel.errors = expressCheckoutResponse.data.cart.errors

        var groupShopModels = ArrayList<GroupShopModel>()
        for (groupShop: GroupShop in expressCheckoutResponse.data.cart.groupShops) {
            groupShopModels.add(getGroupShopModel(groupShop))
        }
        cartModel.groupShopModels = groupShopModels

        dataModel.cartModel = cartModel

        var autoApplyModel = getAutoApplyModel(expressCheckoutResponse)
        dataModel.autoapplyModel = autoApplyModel

        var donationModel = getDonationModel(expressCheckoutResponse)
        dataModel.donationModel = donationModel

        dataModel.success = expressCheckoutResponse.data.success
        dataModel.enablePartialCancel = expressCheckoutResponse.data.enablePartialCancel
        dataModel.errorCode = expressCheckoutResponse.data.errorCode
        dataModel.isCouponActive = expressCheckoutResponse.data.isCouponActive
        dataModel.keroDiscomToken = expressCheckoutResponse.data.keroDiscomToken
        dataModel.keroToken = expressCheckoutResponse.data.keroToken
        dataModel.keroUnixTime = expressCheckoutResponse.data.keroUnixTime
        dataModel.maxCharNote = expressCheckoutResponse.data.maxCharNote ?: 0
        dataModel.maxQuantity = expressCheckoutResponse.data.maxQuantity ?: 0

        var messagesModel = getMessagesModel(expressCheckoutResponse)
        dataModel.messagesModel = messagesModel

        var promoSuggestionModel = getPromoSuggestionModel(expressCheckoutResponse)
        dataModel.promoSuggestionModel = promoSuggestionModel

        var userProfileDefaultModel = getUserProfileModel(expressCheckoutResponse)
        dataModel.userProfileModelDefaultModel = userProfileDefaultModel
        return dataModel
    }

    private fun getHeaderModel(expressCheckoutResponse: ExpressCheckoutResponse): HeaderModel {
        var headerModel = HeaderModel()
        headerModel.errorCode = expressCheckoutResponse.header.errorCode
        headerModel.errors = expressCheckoutResponse.header.errors
        headerModel.processTime = expressCheckoutResponse.header.processTime
        headerModel.reason = expressCheckoutResponse.header.reason
        return headerModel
    }

    private fun getGroupShopModel(groupShop: GroupShop): GroupShopModel {
        var groupShopModel = GroupShopModel()

        var errors = ArrayList<String>()
        errors.addAll(groupShop.errors)
        groupShopModel.errors = errors

        var dropshipperModel = getDropshipperModel(groupShop)
        groupShopModel.dropshiperModel = dropshipperModel;

        groupShopModel.isInsurance = groupShop.isInsurance;

        var messages = ArrayList<String>()
        messages.addAll(groupShop.messages)
        groupShopModel.messages = messages;

        var productModels = ArrayList<ProductModel>()
        for (product: Product in groupShop.products) {
            productModels.add(getProductModel(product))
        }
        groupShopModel.productModels = productModels
        groupShopModel.shippingId = groupShop.shippingId
        groupShopModel.spId = groupShop.spId

        var shopModel = getShopModel(groupShop)
        groupShopModel.shopModel = shopModel

        var shopShipmentModels = ArrayList<ShopShipmentModel>()
        for (shopShiment: ShopShipment in groupShop.shopShipments) {
            shopShipmentModels.add(getShopShipmentModel(shopShiment))
        }
        groupShopModel.shopShipmentModels = shopShipmentModels
        return groupShopModel
    }

    private fun getDropshipperModel(groupShop: GroupShop): DropshipperModel {
        var dropshipperModel = DropshipperModel()
        dropshipperModel.name = groupShop.dropshiper?.name
        dropshipperModel.telpNo = groupShop.dropshiper?.telpNo
        return dropshipperModel
    }

    private fun getProductModel(product: Product): ProductModel {
        var productModel = ProductModel()
        productModel.cartId = product.cartId;
        var errors = ArrayList<String>()
        errors.addAll(product.errors)
        productModel.errors = errors
        productModel.productCashback = product.productCashback
        productModel.productCatId = product.productCatId
        productModel.productCatalogId = product.productCatalogId
        productModel.productCategory = product.productCategory
        productModel.productCondition = product.productCondition
        productModel.productFcancelPartial = product.productFcancelPartial
        productModel.productFinsurance = product.productFinsurance
        productModel.productId = product.productId
        productModel.productImageSrc200Square = product.productImageSrc200Square
        productModel.productInvenageValue = product.productInvenageValue
        productModel.productIsFreeReturns = product.productIsFreeReturns
        productModel.productIsPreorder = product.productIsPreorder
        productModel.productMenuId = product.productMenuId
        productModel.productMinOrder = product.productMinOrder
        productModel.productName = product.productName
        productModel.productNotes = product.productNotes
        productModel.productPrice = product.productPrice
        productModel.productPriceCurrency = product.productPriceCurrency
        productModel.productPriceFmt = product.productPriceFmt
        productModel.productQuantity = product.productQuantity
        productModel.productReturnable = product.productReturnable
        productModel.productSwitchInvenage = product.productSwitchInvenage
        productModel.productUrl = product.productUrl
        productModel.productWeight = product.productWeight
        productModel.productWeightFmt = product.productWeightFmt
        productModel.productWholesalePrice = product.productWholesalePrice
        productModel.productWholesalePriceFmt = product.productWholesalePriceFmt

        var freeReturnsModel = getFreeReturnsModel(product)
        productModel.freeReturnsModel = freeReturnsModel

        var productShipmentMappingModels = ArrayList<ProductShipmentMappingModel>()
        for (productShipmentMapping: ProductShipmentMapping in product.productShipmentMapping) {
            productShipmentMappingModels.add(getProductShipmentMappingModel(productShipmentMapping))
        }
        productModel.productShipmentMappingModels = productShipmentMappingModels

        var productPreorderModel = getProductPreorderModel(product)
        productModel.productPreorderModel = productPreorderModel

        var productShipmentModels = ArrayList<ProductShipmentModel>()
        for (productShipment: ProductShipment in product.productShipment) {
            productShipmentModels.add(getProductShipmentModel(productShipment))
        }
        productModel.productShipmentModels = productShipmentModels

        var productTrackerDataModel = getProductTrackerDataModel(product)
        productModel.productTrackerDataModel = productTrackerDataModel

        var purchaseProtectionPlanDataModel = getPurchaseProtectionPlanDataModel(product)
        productModel.purchaseProtectionPlanDataModel = purchaseProtectionPlanDataModel;

        var productVariantDataModels = ArrayList<ProductVariantDataModel>()
        for (productVariantData: ProductVariantData in product.productVariantData) {
            productVariantDataModels.add(getProductVariantDataModel(productVariantData))
        }
        productModel.productVariantDataModels = productVariantDataModels
        return productModel
    }

    private fun getFreeReturnsModel(product: Product): FreeReturnsModel {
        var freeReturnsModel = FreeReturnsModel()
        freeReturnsModel.freeReturnsLogo = product.freeReturns?.freeReturnsLogo
        return freeReturnsModel
    }

    private fun getProductPreorderModel(product: Product): ProductPreorderModel {
        var productPreorderModel = ProductPreorderModel()
        productPreorderModel.durationDay = product.productPreorder?.durationDay ?: 0
        productPreorderModel.durationText = product.productPreorder?.durationText
        productPreorderModel.durationUnitCode = product.productPreorder?.durationUnitCode ?: 0
        productPreorderModel.durationUnitText = product.productPreorder?.durationUnitText
        productPreorderModel.durationValue = product.productPreorder?.durationValue
        return productPreorderModel
    }

    private fun getProductShipmentModel(productShipment: ProductShipment): ProductShipmentModel {
        var productShipmentModel = ProductShipmentModel()
        productShipmentModel.shipmentId = productShipment.shipmentId
        var serviceIds = ArrayList<String>()
        serviceIds.addAll(productShipment.serviceId)
        productShipmentModel.serviceId = serviceIds
        return productShipmentModel
    }

    private fun getProductTrackerDataModel(product: Product): ProductTrackerDataModel {
        var productTrackerDataModel = ProductTrackerDataModel()
        productTrackerDataModel.attribution = product.productTrackerData?.attribution
        productTrackerDataModel.trackerListName = product.productTrackerData?.trackerListName
        return productTrackerDataModel
    }

    private fun getShopModel(groupShop: GroupShop): ShopModel {
        var shopModel = ShopModel()
        shopModel.addressId = groupShop.shop?.addressId ?: 0
        shopModel.addressStreet = groupShop.shop?.addressStreet
        shopModel.cityId = groupShop.shop?.cityId ?: 0
        shopModel.cityName = groupShop.shop?.cityName
        shopModel.districtId = groupShop.shop?.districtId ?: 0
        shopModel.districtName = groupShop.shop?.districtName
        shopModel.isFreeReturns = groupShop.shop?.isFreeReturns ?: 0
        shopModel.isGold = groupShop.shop?.isGold ?: 0
        shopModel.isGoldBadge = groupShop.shop?.isGoldBadge ?: false
        shopModel.isOfficial = groupShop.shop?.isOfficial ?: 0
        shopModel.latitude = groupShop.shop?.latitude
        shopModel.longitude = groupShop.shop?.longitude
        shopModel.origin = groupShop.shop?.origin ?: 0
        shopModel.postalCode = groupShop.shop?.postalCode
        shopModel.provinceId = groupShop.shop?.provinceId ?: 0
        shopModel.shopId = groupShop.shop?.shopId ?: 0
        shopModel.shopImage = groupShop.shop?.shopImage
        shopModel.shopName = groupShop.shop?.shopName
        shopModel.shopStatus = groupShop.shop?.shopStatus ?: 0
        shopModel.shopUrl = groupShop.shop?.shopUrl
        shopModel.userId = groupShop.shop?.userId ?: 0
        return shopModel
    }

    private fun getShopShipmentModel(shopShiment: ShopShipment): ShopShipmentModel {
        var shopShipmentModel = ShopShipmentModel()
        shopShipmentModel.isDropshipEnabled = shopShiment.isDropshipEnabled
        shopShipmentModel.shipCode = shopShiment.shipCode
        shopShipmentModel.shipId = shopShiment.shipId
        shopShipmentModel.shipLogo = shopShiment.shipLogo
        shopShipmentModel.shipName = shopShiment.shipName

        var shipProdModels = ArrayList<ShipProdModel>()
        for (shipProd: ShipProd in shopShiment.shipProds) {
            shipProdModels.add(getShipProdModel(shipProd))
        }
        shopShipmentModel.shipProdModels = shipProdModels
        return shopShipmentModel
    }

    private fun getShipProdModel(shipProd: ShipProd): ShipProdModel {
        var shipProdModel = ShipProdModel()
        shipProdModel.additionalFee = shipProd.additionalFee
        shipProdModel.minimumWeight = shipProd.minimumWeight
        shipProdModel.shipGroupId = shipProd.shipGroupId
        shipProdModel.shipProdName = shipProd.shipProdName
        shipProdModel.shipProdId = shipProd.shipProdId
        shipProdModel.shipGroupName = shipProd.shipGroupName
        return shipProdModel
    }

    private fun getAutoApplyModel(expressCheckoutResponse: ExpressCheckoutResponse): AutoApplyModel {
        var autoApplyModel = AutoApplyModel()
        autoApplyModel.code = expressCheckoutResponse.data.autoapply?.code
        autoApplyModel.discountAmount = expressCheckoutResponse.data.autoapply?.discountAmount ?: 0
        autoApplyModel.isCoupon = expressCheckoutResponse.data.autoapply?.isCoupon ?: 0
        autoApplyModel.messageSuccess = expressCheckoutResponse.data.autoapply?.messageSuccess
        autoApplyModel.promoId = expressCheckoutResponse.data.autoapply?.promoId ?: 0
        autoApplyModel.isSuccess = expressCheckoutResponse.data.autoapply?.isSuccess
        autoApplyModel.titleDescription = expressCheckoutResponse.data.autoapply?.titleDescription
        return autoApplyModel
    }

    private fun getDonationModel(expressCheckoutResponse: ExpressCheckoutResponse): DonationModel {
        var donationModel = DonationModel()
        donationModel.description = expressCheckoutResponse.data.donation?.description
        donationModel.nominal = expressCheckoutResponse.data.donation?.nominal ?: 0
        donationModel.title = expressCheckoutResponse.data.donation?.title
        return donationModel
    }

    private fun getMessagesModel(expressCheckoutResponse: ExpressCheckoutResponse): MessagesModel {
        var messagesModel = MessagesModel()
        messagesModel.errorCheckoutPriceLimit = expressCheckoutResponse.data.messages?.errorCheckoutPriceLimit
        messagesModel.errorFieldBetween = expressCheckoutResponse.data.messages?.errorFieldBetween
        messagesModel.errorFieldMaxChar = expressCheckoutResponse.data.messages?.errorFieldMaxChar
        messagesModel.errorFieldRequired = expressCheckoutResponse.data.messages?.errorFieldRequired
        messagesModel.errorProductAvailableStock = expressCheckoutResponse.data.messages?.errorProductAvailableStock
        messagesModel.errorProductAvailableStockDetail = expressCheckoutResponse.data.messages?.errorProductAvailableStockDetail
        messagesModel.errorProductMaxQuantity = expressCheckoutResponse.data.messages?.errorProductMaxQuantity
        messagesModel.errorProductMinQuantity = expressCheckoutResponse.data.messages?.errorProductMinQuantity
        return messagesModel
    }

    private fun getPromoSuggestionModel(expressCheckoutResponse: ExpressCheckoutResponse): PromoSuggestionModel {
        var promoSuggestionModel = PromoSuggestionModel()
        promoSuggestionModel.cta = expressCheckoutResponse.data.promoSuggestion?.cta
        promoSuggestionModel.ctaColor = expressCheckoutResponse.data.promoSuggestion?.ctaColor
        promoSuggestionModel.isVisible = expressCheckoutResponse.data.promoSuggestion?.isVisible
        promoSuggestionModel.promoCode = expressCheckoutResponse.data.promoSuggestion?.promoCode
        promoSuggestionModel.text = expressCheckoutResponse.data.promoSuggestion?.text
        return promoSuggestionModel
    }

    private fun getUserProfileModel(expressCheckoutResponse: ExpressCheckoutResponse): UserProfileModel {
        var userProfileDefaultModel = UserProfileModel()
        userProfileDefaultModel.addressId = expressCheckoutResponse.data.userProfileDefault?.addressId
        userProfileDefaultModel.addressStreet = expressCheckoutResponse.data.userProfileDefault?.addressStreet
        userProfileDefaultModel.checkoutParam = expressCheckoutResponse.data.userProfileDefault?.checkoutParam
        userProfileDefaultModel.cityName = expressCheckoutResponse.data.userProfileDefault?.cityName
        userProfileDefaultModel.code = expressCheckoutResponse.data.userProfileDefault?.code
        userProfileDefaultModel.description = expressCheckoutResponse.data.userProfileDefault?.description
        userProfileDefaultModel.districtName = expressCheckoutResponse.data.userProfileDefault?.districtName
        userProfileDefaultModel.gatewayCode = expressCheckoutResponse.data.userProfileDefault?.gatewayCode
        userProfileDefaultModel.image = expressCheckoutResponse.data.userProfileDefault?.image
        userProfileDefaultModel.message = expressCheckoutResponse.data.userProfileDefault?.message
        userProfileDefaultModel.phoneNo = expressCheckoutResponse.data.userProfileDefault?.phoneNo
        userProfileDefaultModel.provinceName = expressCheckoutResponse.data.userProfileDefault?.provinceName
        userProfileDefaultModel.receiverName = expressCheckoutResponse.data.userProfileDefault?.receiverName
        userProfileDefaultModel.serviceId = expressCheckoutResponse.data.userProfileDefault?.serviceId
        userProfileDefaultModel.url = expressCheckoutResponse.data.userProfileDefault?.url
        return userProfileDefaultModel
    }

    private fun getPurchaseProtectionPlanDataModel(product: Product): PurchaseProtectionPlanDataModel {
        var purchaseProtectionPlanDataModel = PurchaseProtectionPlanDataModel()
        purchaseProtectionPlanDataModel.protectionAvailable = product.purchaseProtectionPlanData?.protectionAvailable
        purchaseProtectionPlanDataModel.protectionLinkText = product.purchaseProtectionPlanData?.protectionLinkText
        purchaseProtectionPlanDataModel.protectionLinkUrl = product.purchaseProtectionPlanData?.protectionLinkUrl
        purchaseProtectionPlanDataModel.protectionOptIn = product.purchaseProtectionPlanData?.protectionOptIn
        purchaseProtectionPlanDataModel.protectionPrice = product.purchaseProtectionPlanData?.protectionPrice
        purchaseProtectionPlanDataModel.protectionPricePerProduct = product.purchaseProtectionPlanData?.protectionPricePerProduct
        purchaseProtectionPlanDataModel.protectionSubtitle = product.purchaseProtectionPlanData?.protectionSubtitle
        purchaseProtectionPlanDataModel.protectionTitle = product.purchaseProtectionPlanData?.protectionTitle
        purchaseProtectionPlanDataModel.protectionTypeId = product.purchaseProtectionPlanData?.protectionTypeId
        return purchaseProtectionPlanDataModel
    }

    private fun getProductVariantDataModel(productVariantData: ProductVariantData): ProductVariantDataModel {
        var productVariantDataModel = ProductVariantDataModel()
        productVariantDataModel.parentId = productVariantData.parentId
        productVariantDataModel.defaultChild = productVariantData.defaultChild
        productVariantDataModel.stock = productVariantData.stock
        productVariantDataModel.isEnabled = productVariantData.isEnabled

        var childModels = ArrayList<ChildModel>()
        for (child: Child in productVariantData.children) {
            childModels.add(getChildModel(child))
        }
        productVariantDataModel.childModels = childModels

        var variantModels = ArrayList<VariantModel>()
        for (variant: Variant in productVariantData.variants) {
            variantModels.add(getVariantModel(variant))
        }
        productVariantDataModel.variantModels = variantModels
        return productVariantDataModel
    }

    private fun getChildModel(child: Child): ChildModel {
        var childModel = ChildModel()
        childModel.isBuyable = child.isBuyable
        childModel.isEnabled = child.isEnabled
        childModel.maxOrder = child.maxOrder
        childModel.minOrder = child.minOrder
        childModel.name = child.name
        childModel.price = child.price
        childModel.productId = child.productId
        childModel.sku = child.sku
        childModel.stock = child.stock
        childModel.stockWording = child.stockWording
        childModel.url = child.url
        var optionIds = ArrayList<Int>()
        optionIds.addAll(child.optionIds)
        childModel.optionIds = optionIds
        return childModel
    }

    private fun getVariantModel(variant: Variant): VariantModel {
        var variantModel = VariantModel()
        variantModel.identifier = variant.identifier
        variantModel.position = variant.position
        variantModel.productVariantId = variant.productVariantId
        variantModel.variantName = variant.variantName

        var optionModels = ArrayList<OptionModel>()
        for (option: Option in variant.options) {
            optionModels.add(getOptionModel(option))
        }
        variantModel.optionModels = optionModels
        return variantModel
    }

    private fun getOptionModel(option: Option): OptionModel {
        var optionModel = OptionModel()
        optionModel.hex = option.hex
        optionModel.id = option.id
        optionModel.value = option.value
        return optionModel
    }

    private fun getProductShipmentMappingModel(productShipmentMapping: ProductShipmentMapping): ProductShipmentMappingModel {
        var productShipmentMappingModel = ProductShipmentMappingModel()
        productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
        productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
        var serviceIdModels = ArrayList<ServiceIdModel>()
        for (serviceId: ServiceId in productShipmentMapping.serviceIds) {
            serviceIdModels.add(getServiceIdModel(serviceId))
        }
        productShipmentMappingModel.serviceIdModels = serviceIdModels
        return productShipmentMappingModel
    }

    private fun getServiceIdModel(serviceId: ServiceId): ServiceIdModel {
        var serviceIdModel = ServiceIdModel()
        serviceIdModel.serviceId = serviceId.serviceId
        serviceIdModel.spIds = serviceId.spIds
        return serviceIdModel
    }

}