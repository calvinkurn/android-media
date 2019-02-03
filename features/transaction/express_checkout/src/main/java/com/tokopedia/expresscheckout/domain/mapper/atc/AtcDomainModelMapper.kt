package com.tokopedia.expresscheckout.domain.mapper.atc

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcResponse
import com.tokopedia.expresscheckout.data.entity.response.atc.Message
import com.tokopedia.expresscheckout.data.entity.response.profile.Address
import com.tokopedia.expresscheckout.data.entity.response.profile.Payment
import com.tokopedia.expresscheckout.data.entity.response.profile.Shipment
import com.tokopedia.expresscheckout.domain.model.HeaderModel
import com.tokopedia.expresscheckout.domain.model.atc.*
import com.tokopedia.expresscheckout.domain.model.profile.AddressModel
import com.tokopedia.expresscheckout.domain.model.profile.PaymentModel
import com.tokopedia.expresscheckout.domain.model.profile.ProfileModel
import com.tokopedia.expresscheckout.domain.model.profile.ShipmentModel
import com.tokopedia.transactiondata.entity.response.cartlist.WholesalePrice
import com.tokopedia.transactiondata.entity.response.shippingaddressform.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.ProductVariantData
import com.tokopedia.transactiondata.entity.response.variantdata.Variant
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class AtcDomainModelMapper @Inject constructor() : AtcDataMapper {

    override fun convertToDomainModel(atcResponse: AtcResponse): AtcResponseModel {
        val responseModel = AtcResponseModel()
        if (atcResponse.header != null) {
            responseModel.headerModel = getHeaderModel(atcResponse)
        }
        responseModel.status = atcResponse.status
        responseModel.atcDataModel = getDataModel(atcResponse)
        return responseModel
    }

    private fun getDataModel(atcResponse: AtcResponse): AtcDataModel {
        val dataModel = AtcDataModel()
        dataModel.errorCode = atcResponse.data.errorCode
        dataModel.errors = atcResponse.data.errors
        dataModel.success = atcResponse.data.success

        val cartModel = CartModel()
        cartModel.errors = atcResponse.data.cart.errors

        val groupShopModels = ArrayList<GroupShopModel>()
        for (groupShop: GroupShop in atcResponse.data.cart.groupShops) {
            groupShopModels.add(getGroupShopModel(groupShop))
        }
        cartModel.groupShopModels = groupShopModels

        dataModel.cartModel = cartModel

        val autoApplyModel = getAutoApplyModel(atcResponse)
        dataModel.autoapplyModel = autoApplyModel

        val donationModel = getDonationModel(atcResponse)
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

        val messagesModel = HashMap<String, String>()
        if (atcResponse.data.messages != null) {
            for (message: Message in atcResponse.data.messages) {
                messagesModel[message.index] = message.message
            }
        }
        dataModel.messagesModel = messagesModel

        val promoSuggestionModel = getPromoSuggestionModel(atcResponse)
        dataModel.promoSuggestionModel = promoSuggestionModel

        val userProfileDefaultModel = getUserProfileModel(atcResponse)
        dataModel.userProfileModelDefaultModel = userProfileDefaultModel
        return dataModel
    }

    private fun getHeaderModel(atcResponse: AtcResponse): HeaderModel {
        val headerModel = HeaderModel()
        headerModel.errorCode = atcResponse.header.errorCode
        headerModel.errors = atcResponse.header.errors
        headerModel.processTime = atcResponse.header.processTime
        headerModel.reason = atcResponse.header.reason
        return headerModel
    }

    private fun getGroupShopModel(groupShop: GroupShop): GroupShopModel {
        val groupShopModel = GroupShopModel()

        val errors = ArrayList<String>()
        errors.addAll(groupShop.errors)
        groupShopModel.errors = errors

        val dropshipperModel = getDropshipperModel(groupShop)
        groupShopModel.dropshiperModel = dropshipperModel

        groupShopModel.isInsurance = groupShop.isInsurance

        val messages = ArrayList<String>()
        messages.addAll(groupShop.messages)
        groupShopModel.messages = messages

        val productModels = ArrayList<ProductModel>()
        for (product: Product in groupShop.products) {
            productModels.add(getProductModel(product))
        }
        groupShopModel.productModels = productModels
        groupShopModel.shippingId = groupShop.shippingId
        groupShopModel.spId = groupShop.spId

        val shopModel = getShopModel(groupShop)
        groupShopModel.shopModel = shopModel

        val shopShipmentModels = ArrayList<com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment>()
        for (shopShiment: ShopShipment in groupShop.shopShipments) {
            shopShipmentModels.add(getShopShipmentModel(shopShiment))
        }
        groupShopModel.shopShipmentModels = shopShipmentModels
        return groupShopModel
    }

    private fun getDropshipperModel(groupShop: GroupShop): DropshipperModel {
        val dropshipperModel = DropshipperModel()
        dropshipperModel.name = groupShop.dropshiper?.name
        dropshipperModel.telpNo = groupShop.dropshiper?.telpNo
        return dropshipperModel
    }

    private fun getProductModel(product: Product): ProductModel {
        val productModel = ProductModel()
        productModel.cartId = product.cartId
        val errors = ArrayList<String>()
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

        val freeReturnsModel = getFreeReturnsModel(product)
        productModel.freeReturnsModel = freeReturnsModel

        val productShipmentMappingModels = ArrayList<ProductShipmentMappingModel>()
        for (productShipmentMapping: ProductShipmentMapping in product.productShipmentMapping) {
            productShipmentMappingModels.add(getProductShipmentMappingModel(productShipmentMapping))
        }
        productModel.productShipmentMappingModels = productShipmentMappingModels

        val productPreorderModel = getProductPreorderModel(product)
        productModel.productPreorderModel = productPreorderModel

        val productShipmentModels = ArrayList<ProductShipmentModel>()
        for (productShipment: ProductShipment in product.productShipment) {
            productShipmentModels.add(getProductShipmentModel(productShipment))
        }
        productModel.productShipmentModels = productShipmentModels

        val productTrackerDataModel = getProductTrackerDataModel(product)
        productModel.productTrackerDataModel = productTrackerDataModel

        val purchaseProtectionPlanDataModel = getPurchaseProtectionPlanDataModel(product)
        productModel.purchaseProtectionPlanDataModel = purchaseProtectionPlanDataModel

        val productVariantDataModels = ArrayList<ProductVariantDataModel>()
        productVariantDataModels.add(getProductVariantDataModel(product.productVariantData))
        productModel.productVariantDataModels = productVariantDataModels

        val wholesalePriceModels = ArrayList<WholesalePriceModel>()
        if (product.wholesalePrice != null) {
            for (wholesalePrice: WholesalePrice in product.wholesalePrice) {
                wholesalePriceModels.add(getWholesalePriceModel(wholesalePrice))
            }
        }
        return productModel
    }

    private fun getWholesalePriceModel(wholesalePrice: WholesalePrice): WholesalePriceModel {
        val wholesalePriceModel = WholesalePriceModel()
        wholesalePriceModel.prdPrc = wholesalePrice.prdPrc
        wholesalePriceModel.prdPrcFmt = wholesalePrice.prdPrcFmt
        wholesalePriceModel.qtyMin = wholesalePrice.qtyMin
        wholesalePriceModel.qtyMinFmt = wholesalePrice.qtyMinFmt
        wholesalePriceModel.qtyMax = wholesalePrice.qtyMax
        wholesalePriceModel.qtyMaxFmt = wholesalePrice.qtyMaxFmt
        return wholesalePriceModel
    }

    private fun getFreeReturnsModel(product: Product): FreeReturnsModel {
        val freeReturnsModel = FreeReturnsModel()
        freeReturnsModel.freeReturnsLogo = product.freeReturns?.freeReturnsLogo
        return freeReturnsModel
    }

    private fun getProductPreorderModel(product: Product): ProductPreorderModel {
        val productPreorderModel = ProductPreorderModel()
        productPreorderModel.durationDay = product.productPreorder?.durationDay ?: 0
        productPreorderModel.durationText = product.productPreorder?.durationText
        productPreorderModel.durationUnitCode = product.productPreorder?.durationUnitCode ?: 0
        productPreorderModel.durationUnitText = product.productPreorder?.durationUnitText
        productPreorderModel.durationValue = product.productPreorder?.durationValue
        return productPreorderModel
    }

    private fun getProductShipmentModel(productShipment: ProductShipment): ProductShipmentModel {
        val productShipmentModel = ProductShipmentModel()
        productShipmentModel.shipmentId = productShipment.shipmentId
        val serviceIds = ArrayList<String>()
        serviceIds.addAll(productShipment.serviceId)
        productShipmentModel.serviceId = serviceIds
        return productShipmentModel
    }

    private fun getProductTrackerDataModel(product: Product): ProductTrackerDataModel {
        val productTrackerDataModel = ProductTrackerDataModel()
        productTrackerDataModel.attribution = product.productTrackerData?.attribution
        productTrackerDataModel.trackerListName = product.productTrackerData?.trackerListName
        return productTrackerDataModel
    }

    private fun getShopModel(groupShop: GroupShop): ShopModel {
        val shopModel = ShopModel()
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
        val shopShipment = com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment()
        shopShipment.isDropshipEnabled = shopShiment.isDropshipEnabled == 1
        shopShipment.shipCode = shopShiment.shipCode
        shopShipment.shipId = shopShiment.shipId
        shopShipment.shipLogo = shopShiment.shipLogo
        shopShipment.shipName = shopShiment.shipName

        val shipProds = ArrayList<com.tokopedia.shipping_recommendation.domain.shipping.ShipProd>()
        for (shipProd: ShipProd in shopShiment.shipProds) {
            shipProds.add(getShipProdModel(shipProd))
        }
        shopShipment.shipProds = shipProds
        return shopShipment
    }

    private fun getShipProdModel(shipProd: ShipProd): com.tokopedia.shipping_recommendation.domain.shipping.ShipProd {
        val shipProdModel = com.tokopedia.shipping_recommendation.domain.shipping.ShipProd()
        shipProdModel.additionalFee = shipProd.additionalFee
        shipProdModel.minimumWeight = shipProd.minimumWeight
        shipProdModel.shipGroupId = shipProd.shipGroupId
        shipProdModel.shipProdName = shipProd.shipProdName
        shipProdModel.shipProdId = shipProd.shipProdId
        shipProdModel.shipGroupName = shipProd.shipGroupName
        return shipProdModel
    }

    private fun getAutoApplyModel(atcResponse: AtcResponse): AutoApplyModel {
        val autoApplyModel = AutoApplyModel()
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
        val donationModel = DonationModel()
        donationModel.description = atcResponse.data.donation?.description
        donationModel.nominal = atcResponse.data.donation?.nominal ?: 0
        donationModel.title = atcResponse.data.donation?.title
        return donationModel
    }

    private fun getPromoSuggestionModel(atcResponse: AtcResponse): PromoSuggestionModel {
        val promoSuggestionModel = PromoSuggestionModel()
        promoSuggestionModel.cta = atcResponse.data.promoSuggestion?.cta
        promoSuggestionModel.ctaColor = atcResponse.data.promoSuggestion?.ctaColor
        promoSuggestionModel.isVisible = atcResponse.data.promoSuggestion?.isVisible
        promoSuggestionModel.promoCode = atcResponse.data.promoSuggestion?.promoCode
        promoSuggestionModel.text = atcResponse.data.promoSuggestion?.text
        return promoSuggestionModel
    }

    private fun getUserProfileModel(atcResponse: AtcResponse): ProfileModel {
        val userProfileDefaultModel = ProfileModel()
        userProfileDefaultModel.id = atcResponse.data.userProfileDefault?.id ?: 0
        userProfileDefaultModel.status = atcResponse.data.userProfileDefault?.status ?: 0
        userProfileDefaultModel.addressModel = getAddressModel(atcResponse.data.userProfileDefault?.address)
        userProfileDefaultModel.shipmentModel = getShipmentModel(atcResponse.data.userProfileDefault?.shipment)
        userProfileDefaultModel.paymentModel = getPaymentModel(atcResponse.data.userProfileDefault?.payment)

        return userProfileDefaultModel
    }

    private fun getAddressModel(address: Address?): AddressModel {
        val addressModel = AddressModel()
        addressModel.addressId = address?.addressId ?: 0
        addressModel.addressName = address?.addressName
        addressModel.addressStreet = address?.addressStreet
        addressModel.cityId = address?.cityId ?: 0
        addressModel.cityName = address?.cityName
        addressModel.districtId = address?.districtId ?: 0
        addressModel.districtName = address?.districtName
        addressModel.latitude = address?.latitude
        addressModel.longitude = address?.longitude
        addressModel.phone = address?.phone
        addressModel.provinceId = address?.provinceId ?: 0
        addressModel.provinceName = address?.provinceName
        addressModel.receiverName = address?.receiverName
        addressModel.postalCode = address?.postalCode

        return addressModel
    }

    private fun getPaymentModel(payment: Payment?): PaymentModel {
        val paymentModel = PaymentModel()
        paymentModel.checkoutParam = payment?.checkoutParam
        paymentModel.description = payment?.description
        paymentModel.gatewayCode = payment?.gatewayCode
        paymentModel.image = payment?.image
        paymentModel.url = payment?.url

        return paymentModel
    }

    private fun getShipmentModel(shipment: Shipment?): ShipmentModel {
        val shipmentModel = ShipmentModel()
        shipmentModel.serviceId = shipment?.serviceId ?: 0
        shipmentModel.serviceDuration = shipment?.serviceDuration

        return shipmentModel
    }

    private fun getPurchaseProtectionPlanDataModel(product: Product): PurchaseProtectionPlanDataModel {
        val purchaseProtectionPlanDataModel = PurchaseProtectionPlanDataModel()
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
        val productVariantDataModel = ProductVariantDataModel()
        productVariantDataModel.parentId = productVariantData.parentId
        productVariantDataModel.defaultChild = productVariantData.defaultChild
        productVariantDataModel.stock = productVariantData.stock
        productVariantDataModel.isEnabled = productVariantData.isEnabled

        val childModels = ArrayList<ChildModel>()
        for (child: Child in productVariantData.children) {
            childModels.add(getChildModel(child))
        }
        productVariantDataModel.childModels = childModels

        val variantModels = ArrayList<VariantModel>()
        for (variant: Variant in productVariantData.variants) {
            variantModels.add(getVariantModel(variant))
        }
        productVariantDataModel.variantModels = variantModels
        return productVariantDataModel
    }

    private fun getChildModel(child: Child): ChildModel {
        val childModel = ChildModel()
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
        val optionIds = ArrayList<Int>()
        optionIds.addAll(child.optionIds)
        childModel.optionIds = optionIds
        return childModel
    }

    private fun getVariantModel(variant: Variant): VariantModel {
        val variantModel = VariantModel()
        variantModel.identifier = variant.identifier
        variantModel.position = variant.position
        variantModel.productVariantId = variant.productVariantId
        variantModel.variantName = variant.variantName

        val optionModels = ArrayList<OptionModel>()
        for (option: Option in variant.options) {
            optionModels.add(getOptionModel(option))
        }
        variantModel.optionModels = optionModels
        return variantModel
    }

    private fun getOptionModel(option: Option): OptionModel {
        val optionModel = OptionModel()
        optionModel.hex = option.hex
        optionModel.id = option.id
        optionModel.value = option.value
        return optionModel
    }

    private fun getProductShipmentMappingModel(productShipmentMapping: ProductShipmentMapping): ProductShipmentMappingModel {
        val productShipmentMappingModel = ProductShipmentMappingModel()
        productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
        productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
        val serviceIdModels = ArrayList<ServiceIdModel>()
        for (serviceId: ServiceId in productShipmentMapping.serviceIds) {
            serviceIdModels.add(getServiceIdModel(serviceId))
        }
        productShipmentMappingModel.serviceIdModels = serviceIdModels
        return productShipmentMappingModel
    }

    private fun getServiceIdModel(serviceId: ServiceId): ServiceIdModel {
        val serviceIdModel = ServiceIdModel()
        serviceIdModel.serviceId = serviceId.serviceId
        serviceIdModel.spIds = serviceId.spIds
        return serviceIdModel
    }

}