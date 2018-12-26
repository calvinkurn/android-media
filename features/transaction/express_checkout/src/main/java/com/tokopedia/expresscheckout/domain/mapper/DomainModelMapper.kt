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

    override fun convertToDomainModel(expressCheckoutResponse: ExpressCheckoutResponse): ResponseModel {

        var responseModel = ResponseModel()
        responseModel.status = expressCheckoutResponse.status

        var headerModel = HeaderModel()
        headerModel.errorCode = expressCheckoutResponse.header.errorCode
        headerModel.errors = expressCheckoutResponse.header.errors
        headerModel.processTime = expressCheckoutResponse.header.processTime
        headerModel.reason = expressCheckoutResponse.header.reason
        responseModel.headerModel = headerModel

        var dataModel = DataModel()
        dataModel.errorCode = expressCheckoutResponse.data.errorCode
        dataModel.errors = expressCheckoutResponse.data.errors
        dataModel.success = expressCheckoutResponse.data.success

        var cartModel = CartModel()
        cartModel.errors = expressCheckoutResponse.data.cart.errors

        var groupShopModels = ArrayList<GroupShopModel>()
        for (groupShop: GroupShop in expressCheckoutResponse.data.cart.groupShops) {
            var groupShopModel = GroupShopModel()

            var errors = ArrayList<String>()
            errors.addAll(groupShop.errors)
            groupShopModel.errors = errors

            var dropshipperModel = DropshipperModel()
            dropshipperModel.name = groupShop.dropshiper?.name
            dropshipperModel.telpNo = groupShop.dropshiper?.telpNo
            groupShopModel.dropshiperModel = dropshipperModel;

            groupShopModel.isInsurance = groupShop.isInsurance;

            var messages = ArrayList<String>()
            messages.addAll(groupShop.messages)
            groupShopModel.messages = messages;

            var productModels = ArrayList<ProductModel>()
            for (product: Product in groupShop.products) {
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

                var freeReturnsModel = FreeReturnsModel()
                freeReturnsModel.freeReturnsLogo = product.freeReturns?.freeReturnsLogo
                productModel.freeReturnsModel = freeReturnsModel

                var productShipmentMappingModels = ArrayList<ProductShipmentMappingModel>()
                for (productShipmentMapping: ProductShipmentMapping in product.productShipmentMapping) {
                    var productShipmentMappingModel = ProductShipmentMappingModel()
                    productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
                    productShipmentMappingModel.shipmentId = productShipmentMapping.shipmentId
                    var serviceIdModels = ArrayList<ServiceIdModel>()
                    for (serviceId: ServiceId in productShipmentMapping.serviceIds) {
                        var serviceIdModel = ServiceIdModel()
                        serviceIdModel.serviceId = serviceId.serviceId
                        serviceIdModel.spIds = serviceId.spIds
                        serviceIdModels.add(serviceIdModel)
                    }
                    productShipmentMappingModel.serviceIdModels = serviceIdModels
                    productShipmentMappingModels.add(productShipmentMappingModel)
                }
                productModel.productShipmentMappingModels = productShipmentMappingModels

                var productPreorderModel = ProductPreorderModel()
                productPreorderModel.durationDay = product.productPreorder?.durationDay ?: 0
                productPreorderModel.durationText = product.productPreorder?.durationText
                productPreorderModel.durationUnitCode = product.productPreorder?.durationUnitCode ?: 0
                productPreorderModel.durationUnitText = product.productPreorder?.durationUnitText
                productPreorderModel.durationValue = product.productPreorder?.durationValue
                productModel.productPreorderModel = productPreorderModel

                var productShipmentModels = ArrayList<ProductShipmentModel>()
                for (productShipment: ProductShipment in product.productShipment) {
                    var productShipmentModel = ProductShipmentModel()
                    productShipmentModel.shipmentId = productShipment.shipmentId
                    var serviceIds = ArrayList<String>()
                    serviceIds.addAll(productShipment.serviceId)
                    productShipmentModel.serviceId = serviceIds
                    productShipmentModels.add(productShipmentModel)
                }
                productModel.productShipmentModels = productShipmentModels

                var productTrackerDataModel = ProductTrackerDataModel()
                productTrackerDataModel.attribution = product.productTrackerData?.attribution
                productTrackerDataModel.trackerListName = product.productTrackerData?.trackerListName
                productModel.productTrackerDataModel = productTrackerDataModel

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
                productModel.purchaseProtectionPlanDataModel = purchaseProtectionPlanDataModel;

                var productVariantDataModels = ArrayList<ProductVariantDataModel>()
                for (productVariantData: ProductVariantData in product.productVariantData) {
                    var productVariantDataModel = ProductVariantDataModel()
                    productVariantDataModel.parentId = productVariantData.parentId
                    productVariantDataModel.defaultShild = productVariantData.defaultChild
                    productVariantDataModel.stock = productVariantData.stock
                    productVariantDataModel.isEnabled = productVariantData.isEnabled

                    var childModels = ArrayList<ChildModel>()
                    for (child: Child in productVariantData.children) {
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
                        childModels.add(childModel)
                    }
                    productVariantDataModel.childModels = childModels

                    var variantModels = ArrayList<VariantModel>()
                    for (variant: Variant in productVariantData.variants) {
                        var variantModel = VariantModel()
                        variantModel.identifier = variant.identifier
                        variantModel.position = variant.position
                        variantModel.productVariantId = variant.productVariantId
                        variantModel.variantName = variant.variantName

                        var optionModels = ArrayList<OptionModel>()
                        for (option: Option in variant.options) {
                            var optionModel = OptionModel()
                            optionModel.hex = option.hex
                            optionModel.id = option.id
                            optionModel.value = option.value
                            optionModels.add(optionModel)
                        }
                        variantModel.optionModels = optionModels

                        variantModels.add(variantModel)
                    }
                    productVariantDataModel.variantModels = variantModels

                    productVariantDataModels.add(productVariantDataModel)
                }
                productModel.productVariantDataModels = productVariantDataModels
                productModels.add(productModel)
            }
            groupShopModel.productModels = productModels
            groupShopModel.shippingId = groupShop.shippingId
            groupShopModel.spId = groupShop.spId

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
            groupShopModel.shopModel = shopModel

            var shopShipmentModels = ArrayList<ShopShipmentModel>()
            for () {

            }
            groupShopModel.shopShipmentModels = shopShiomentModels

            groupShopModels.add(groupShopModel)
        }
        cartModel.groupShopModels = groupShopModels

        dataModel.cartModel = cartModel
        dataModel.autoapply
        dataModel.donationModel
        dataModel.enablePartialCancel
        dataModel.errorCode
        dataModel.isCouponActive
        dataModel.keroDiscomToken
        dataModel.keroToken
        dataModel.keroUnixTime
        dataModel.maxCharNote
        dataModel.maxQuantity
        dataModel.messagesModel
        dataModel.promoSuggestionModel
        dataModel.success
        dataModel.userProfileModelDefault

        return responseModel
    }

}