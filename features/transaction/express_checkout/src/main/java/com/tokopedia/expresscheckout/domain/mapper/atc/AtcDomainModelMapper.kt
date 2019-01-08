package com.tokopedia.expresscheckout.domain.mapper.atc

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcResponse
import com.tokopedia.expresscheckout.domain.model.HeaderModel
import com.tokopedia.expresscheckout.domain.model.atc.*
import com.tokopedia.transactiondata.entity.response.shippingaddressform.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.ProductVariantData
import com.tokopedia.transactiondata.entity.response.variantdata.Variant

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class AtcDomainModelMapper : AtcDataMapper {

    override fun convertToDomainModel(atcResponse: AtcResponse): AtcResponseModel {
        var responseModel = AtcResponseModel()
        if (atcResponse.header != null) {
            responseModel.headerModel = getHeaderModel(atcResponse)
        }
        responseModel.status = atcResponse.status
        responseModel.atcDataModel = getDataModel(atcResponse)
        return responseModel
    }

    private fun getDataModel(atcResponse: AtcResponse): AtcDataModel {
        var dataModel = AtcDataModel()
        dataModel.errorCode = atcResponse.data.errorCode
        dataModel.errors = atcResponse.data.errors
        dataModel.success = atcResponse.data.success

        var cartModel = CartModel()
        cartModel.errors = atcResponse.data.cart.errors

        var groupShopModels = ArrayList<GroupShopModel>()
        for (groupShop: GroupShop in atcResponse.data.cart.groupShops) {
            groupShopModels.add(getGroupShopModel(groupShop))
        }
        cartModel.groupShopModels = groupShopModels

        dataModel.cartModel = cartModel

        var autoApplyModel = getAutoApplyModel(atcResponse)
        dataModel.autoapplyModel = autoApplyModel

        var donationModel = getDonationModel(atcResponse)
        dataModel.donationModel = donationModel

        dataModel.success = atcResponse.data.success
        dataModel.enablePartialCancel = atcResponse.data.enablePartialCancel
        dataModel.errorCode = atcResponse.data.errorCode
        dataModel.isCouponActive = atcResponse.data.isCouponActive
        dataModel.keroDiscomToken = atcResponse.data.keroDiscomToken
        dataModel.keroToken = atcResponse.data.keroToken
        dataModel.keroUnixTime = atcResponse.data.keroUnixTime
        dataModel.maxCharNote = atcResponse.data.maxCharNote ?: 0
        dataModel.maxQuantity = atcResponse.data.maxQuantity ?: 0

        var messagesModel = getMessagesModel(atcResponse)
        dataModel.messagesModel = messagesModel

        var promoSuggestionModel = getPromoSuggestionModel(atcResponse)
        dataModel.promoSuggestionModel = promoSuggestionModel

        var userProfileDefaultModel = getUserProfileModel(atcResponse)
        dataModel.userProfileModelDefaultModel = userProfileDefaultModel
        return dataModel
    }

    private fun getHeaderModel(atcResponse: AtcResponse): HeaderModel {
        var headerModel = HeaderModel()
        headerModel.errorCode = atcResponse.header.errorCode
        headerModel.errors = atcResponse.header.errors
        headerModel.processTime = atcResponse.header.processTime
        headerModel.reason = atcResponse.header.reason
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

        var shopShipmentModels = ArrayList<com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment>()
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
        productVariantDataModels.add(getProductVariantDataModel(product.productVariantData))
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

    private fun getShopShipmentModel(shopShiment: ShopShipment): com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment {
        var shopShipment = com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment()
        shopShipment.isDropshipEnabled = shopShiment.isDropshipEnabled == 1
        shopShipment.shipCode = shopShiment.shipCode
        shopShipment.shipId = shopShiment.shipId
        shopShipment.shipLogo = shopShiment.shipLogo
        shopShipment.shipName = shopShiment.shipName

        var shipProds = ArrayList<com.tokopedia.shipping_recommendation.domain.shipping.ShipProd>()
        for (shipProd: ShipProd in shopShiment.shipProds) {
            shipProds.add(getShipProdModel(shipProd))
        }
        shopShipment.shipProds = shipProds
        return shopShipment
    }

    private fun getShipProdModel(shipProd: ShipProd): com.tokopedia.shipping_recommendation.domain.shipping.ShipProd {
        var shipProd = com.tokopedia.shipping_recommendation.domain.shipping.ShipProd()
        shipProd.additionalFee = shipProd.additionalFee
        shipProd.minimumWeight = shipProd.minimumWeight
        shipProd.shipGroupId = shipProd.shipGroupId
        shipProd.shipProdName = shipProd.shipProdName
        shipProd.shipProdId = shipProd.shipProdId
        shipProd.shipGroupName = shipProd.shipGroupName
        return shipProd
    }

    private fun getAutoApplyModel(atcResponse: AtcResponse): AutoApplyModel {
        var autoApplyModel = AutoApplyModel()
        autoApplyModel.code = atcResponse.data.autoapply?.code
        autoApplyModel.discountAmount = atcResponse.data.autoapply?.discountAmount ?: 0
        autoApplyModel.isCoupon = atcResponse.data.autoapply?.isCoupon ?: 0
        autoApplyModel.messageSuccess = atcResponse.data.autoapply?.messageSuccess
        autoApplyModel.promoId = atcResponse.data.autoapply?.promoId ?: 0
        autoApplyModel.isSuccess = atcResponse.data.autoapply?.isSuccess
        autoApplyModel.titleDescription = atcResponse.data.autoapply?.titleDescription
        return autoApplyModel
    }

    private fun getDonationModel(atcResponse: AtcResponse): DonationModel {
        var donationModel = DonationModel()
        donationModel.description = atcResponse.data.donation?.description
        donationModel.nominal = atcResponse.data.donation?.nominal ?: 0
        donationModel.title = atcResponse.data.donation?.title
        return donationModel
    }

    private fun getMessagesModel(atcResponse: AtcResponse): MessagesModel {
        var messagesModel = MessagesModel()
        messagesModel.errorCheckoutPriceLimit = atcResponse.data.messages?.errorCheckoutPriceLimit
        messagesModel.errorFieldBetween = atcResponse.data.messages?.errorFieldBetween
        messagesModel.errorFieldMaxChar = atcResponse.data.messages?.errorFieldMaxChar
        messagesModel.errorFieldRequired = atcResponse.data.messages?.errorFieldRequired
        messagesModel.errorProductAvailableStock = atcResponse.data.messages?.errorProductAvailableStock
        messagesModel.errorProductAvailableStockDetail = atcResponse.data.messages?.errorProductAvailableStockDetail
        messagesModel.errorProductMaxQuantity = atcResponse.data.messages?.errorProductMaxQuantity
        messagesModel.errorProductMinQuantity = atcResponse.data.messages?.errorProductMinQuantity
        return messagesModel
    }

    private fun getPromoSuggestionModel(atcResponse: AtcResponse): PromoSuggestionModel {
        var promoSuggestionModel = PromoSuggestionModel()
        promoSuggestionModel.cta = atcResponse.data.promoSuggestion?.cta
        promoSuggestionModel.ctaColor = atcResponse.data.promoSuggestion?.ctaColor
        promoSuggestionModel.isVisible = atcResponse.data.promoSuggestion?.isVisible
        promoSuggestionModel.promoCode = atcResponse.data.promoSuggestion?.promoCode
        promoSuggestionModel.text = atcResponse.data.promoSuggestion?.text
        return promoSuggestionModel
    }

    private fun getUserProfileModel(atcResponse: AtcResponse): UserProfileModel {
        var userProfileDefaultModel = UserProfileModel()
        userProfileDefaultModel.addressId = atcResponse.data.userProfileDefault?.addressId
        userProfileDefaultModel.addressStreet = atcResponse.data.userProfileDefault?.addressStreet
        userProfileDefaultModel.checkoutParam = atcResponse.data.userProfileDefault?.checkoutParam
        userProfileDefaultModel.cityName = atcResponse.data.userProfileDefault?.cityName
        userProfileDefaultModel.code = atcResponse.data.userProfileDefault?.code
        userProfileDefaultModel.description = atcResponse.data.userProfileDefault?.description
        userProfileDefaultModel.districtName = atcResponse.data.userProfileDefault?.districtName
        userProfileDefaultModel.gatewayCode = atcResponse.data.userProfileDefault?.gatewayCode
        userProfileDefaultModel.image = atcResponse.data.userProfileDefault?.image
        userProfileDefaultModel.message = atcResponse.data.userProfileDefault?.message
        userProfileDefaultModel.phoneNo = atcResponse.data.userProfileDefault?.phoneNo
        userProfileDefaultModel.provinceName = atcResponse.data.userProfileDefault?.provinceName
        userProfileDefaultModel.receiverName = atcResponse.data.userProfileDefault?.receiverName
        userProfileDefaultModel.serviceId = atcResponse.data.userProfileDefault?.serviceId
        userProfileDefaultModel.url = atcResponse.data.userProfileDefault?.url
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