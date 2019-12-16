package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.datamodel.*

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): MutableList<DynamicPDPDataModel> {
        val listOfComponent: MutableList<DynamicPDPDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                ProductDetailConstant.PRODUCT_SNAPSHOT -> {
                    listOfComponent.add(ProductSnapshotDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.DISCUSSION -> {
                    listOfComponent.add(ProductDiscussionDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_INFO -> {
                    listOfComponent.add(ProductInfoDataModel(mapToProductInfoContent(component.componentData), type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SHOP_INFO -> {
                    listOfComponent.add(ProductShopInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SOCIAL_PROOF -> {
                    listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.IMAGE_REVIEW -> {
                    listOfComponent.add(ProductImageReviewDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MOST_HELPFUL_REVIEW -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.INFO -> {
                    val contentData = component.componentData.firstOrNull()
                    listOfComponent.add(ProductGeneralInfoDataModel(ProductGeneralInfoData(contentData?.applink
                            ?: "", contentData?.title ?: "", contentData?.content
                            ?: listOf()), type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_LIST -> {
                    listOfComponent.add(ProductRecommendationDataModel(type = component.type, name = component.componentName, position = index))
                }
                ProductDetailConstant.SHOP_VOUCHER -> {
                    listOfComponent.add(ProductMerchantVoucherDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SEPARATOR -> {
                    listOfComponent.add(SeparatorDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.VALUE_PROPOSITION -> {
                    listOfComponent.add(ProductValuePropositionDataModel(type = component.type, name = component.componentName))
                }
            }
        }
        return listOfComponent
    }

    fun mapToDynamicProductDetailP1(data: PdpGetLayout): DynamicProductInfoP1 {
        val componentData = data.components.find {
            it.type == ProductDetailConstant.PRODUCT_SNAPSHOT
        }?.componentData?.firstOrNull() ?: ComponentData()

        return DynamicProductInfoP1(basic = data.basicInfo, data = componentData)
    }

    fun mapProductInfoToDynamicProductInfo(newData: ProductInfo, oldData: DynamicProductInfoP1): DynamicProductInfoP1 {
        return oldData.apply {
            basic.copy(
                    alias = newData.basic.alias,
                    catalogID = newData.basic.catalogID.toString(),
                    category = newData.category,
                    gtin = newData.basic.gtin,
                    isKreasiLokal = newData.basic.isKreasiLokal,
                    isLeasing = newData.basic.isLeasing,
                    isMustInsurance = newData.basic.isMustInsurance,
                    maxOrder = newData.basic.maxOrder,
                    minOrder = newData.basic.minOrder,
                    menu = newData.menu,
                    needPrescription = newData.basic.needPrescription,
                    productID = newData.basic.id.toString(),
                    shopID = newData.basic.shopID.toString(),
                    sku = newData.basic.sku,
                    status = newData.basic.status,
                    url = newData.basic.url,
                    condition = newData.basic.condition,
                    weightUnit = newData.basic.weightUnit)

            val campaignData = data.campaign
            val mediaCopy: List<Media> = data.media.map {
                Media(it.description, it.isAutoplay, it.type, it.uRL300, it.uRLOriginal,
                        it.uRLThumbnail, it.videoURLAndroid, it.videoURLIOS)
            }
            val wholesaleCopy: List<Wholesale> = newData.wholesale?.map {
                Wholesale(WholesalePrice(value = it.price.toInt()), it.minQty)
            } ?: listOf()

            data.copy(
                    campaign = CampaignModular(campaignData.appLinks, campaignData.campaignID, campaignData.campaignType,
                            campaignData.campaignTypeName, campaignData.discountedPrice, campaignData.endDate, campaignData.endDateUnix,
                            campaignData.hideGimmick, campaignData.isActive, campaignData.isAppsOnly, campaignData.originalPrice,
                            campaignData.percentageAmount, campaignData.startDate, campaignData.stock),
                    isCOD = newData.basic.isEligibleCod,
                    isCashback = newData.cashback,
                    isFreeOngkir = IsFreeOngkir(newData.freeOngkir.freeOngkirImgUrl, newData.freeOngkir.isFreeOngkirActive),
                    media = mediaCopy,
                    pictures = newData.pictures ?: listOf(),
                    price = Price(newData.basic.priceCurrency, newData.basic.lastUpdatePrice, newData.basic.price.toInt()),
                    stock = newData.stock,
                    variant = newData.variant,
                    videos = newData.videos,
                    wholesale = wholesaleCopy,
                    preOrder = newData.preorder,
                    name = newData.basic.name
            )
        }
    }

    fun hashMapLayout(data: List<DynamicPDPDataModel>): Map<String, DynamicPDPDataModel> {
        return data.associateBy({
            if (it.type() != it.name()) it.name() else it.type()
        }, {
            it
        })
    }

    fun mapToWholesale(data: List<Wholesale>?): List<com.tokopedia.product.detail.common.data.model.product.Wholesale>? {
        return if (data == null || data.isEmpty()) {
            null
        } else {
            data.map {
                com.tokopedia.product.detail.common.data.model.product.Wholesale(it.minQty, it.price.value.toFloat())
            }
        }
    }


    private fun mapToProductInfoContent(listOfData: List<ComponentData>): List<ProductInfoContent>? {
        return if (listOfData.isEmpty()) {
            null
        } else {
            listOfData.map {
                ProductInfoContent(it.row, it.content)
            }
        }
    }
}