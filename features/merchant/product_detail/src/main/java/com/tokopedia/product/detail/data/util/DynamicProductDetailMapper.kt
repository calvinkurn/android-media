package com.tokopedia.product.detail.data.util

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.data.model.pdplayout.Component
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.OneLinersContent
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.Wholesale
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.affiliate.AffiliateUIIDRequest
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCategoryCarouselDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofStockDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.data.model.review.ImageReview
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_7
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_9_TOKONOW
import com.tokopedia.product.detail.data.util.ProductDetailConstant.SHOPADS_CAROUSEL
import com.tokopedia.product.detail.view.util.checkIfNumber
import com.tokopedia.product.share.ProductData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.Shop

object DynamicProductDetailMapper {

    /**
     * Map network data into UI data by type, just assign type and name here. The data will be assigned in fragment
     * except info type
     * If data already complete at P1 call, assign the value here.
     */
    fun mapIntoVisitable(data: List<Component>): MutableList<DynamicPdpDataModel> {
        val listOfComponent: MutableList<DynamicPdpDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                ProductDetailConstant.NOTIFY_ME -> {
                    listOfComponent.add(ProductNotifyMeDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.DISCUSSION_FAQ -> {
                    listOfComponent.add(ProductDiscussionMostHelpfulDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_DETAIL -> {
                    listOfComponent.add(ProductDetailInfoDataModel(type = component.type, name = component.componentName, dataContent = mapToProductDetailInfoContent(component.componentData.firstOrNull())))
                }
                ProductDetailConstant.MINI_SOCIAL_PROOF -> {
                    listOfComponent.add(ProductMiniSocialProofDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK -> {
                    listOfComponent.add(ProductMiniSocialProofStockDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.REVIEW -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.INFO -> {
                    val contentData = component.componentData.firstOrNull()
                    val content = if (contentData?.content?.isEmpty() == true) listOf(Content()) else contentData?.content
                    listOfComponent.add(ProductGeneralInfoDataModel(component.componentName, component.type, contentData?.applink
                            ?: "", contentData?.title ?: "",
                            contentData?.isApplink ?: true, contentData?.icon
                            ?: "", content?.firstOrNull()?.subtitle
                            ?: "", content?.firstOrNull()?.icon ?: "")
                    )
                }
                ProductDetailConstant.MINI_SHOP_WIDGET -> {
                    listOfComponent.add(ProductMiniShopWidgetDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_LIST -> {
                    when (component.componentName) {
                        PDP_7, PDP_9_TOKONOW ->
                            listOfComponent.add(ProductRecomWidgetDataModel(type = component.type, name = component.componentName, position = index))
                        SHOPADS_CAROUSEL -> {
                            listOfComponent.add(TopadsHeadlineUiModel(type = component.type, name = component.componentName))
                        }
                        else ->
                            listOfComponent.add(ProductRecommendationDataModel(type = component.type, name = component.componentName, position = index))
                    }
                }
                ProductDetailConstant.VARIANT -> {
                    if (component.componentName == ProductDetailConstant.MINI_VARIANT_OPTIONS) {
                        listOfComponent.add(ProductSingleVariantDataModel(type = component.type, name = component.componentName))
                    } else {
                        listOfComponent.add(VariantDataModel(type = component.type, name = component.componentName))
                    }
                }
                ProductDetailConstant.PRODUCT_CONTENT -> {
                    listOfComponent.add(ProductContentDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MEDIA -> {
                    listOfComponent.add(ProductMediaDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.TICKER_INFO -> {
                    listOfComponent.add(ProductTickerInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY -> {
                    listOfComponent.add(ProductShopCredibilityDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_CUSTOM_INFO -> {
                    val contentData = component.componentData.firstOrNull()
                    val customInfoData = mapToCustomInfoUiModel(contentData, component.componentName, component.type)

                    customInfoData?.let {
                        listOfComponent.add(it)
                    }
                }
                ProductDetailConstant.TOP_ADS -> {
                    listOfComponent.add(TopAdsImageDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.REPORT -> {
                    listOfComponent.add(ProductReportDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SHIPMENT -> {
                    listOfComponent.add(ProductShipmentDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MVC -> {
                    listOfComponent.add(ProductMerchantVoucherSummaryDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.ONE_LINERS -> {
                    listOfComponent.add(
                            OneLinersDataModel(type = component.type, name = component.componentName)
                    )
                }
                ProductDetailConstant.CATEGORY_CAROUSEL -> {
                    //all data already provided in here (P1), so fill the data
                    val carouselData = component.componentData.firstOrNull()

                    if (carouselData?.categoryCarouselList?.isNotEmpty() == true) {
                        listOfComponent.add(
                                ProductCategoryCarouselDataModel(type = component.type,
                                        name = component.componentName,
                                        titleCarousel = carouselData.titleCarousel,
                                        linkText = carouselData.linkText,
                                        applink = carouselData.applink,
                                        categoryList = carouselData.categoryCarouselList))
                    }
                }
                ProductDetailConstant.PRODUCT_BUNDLING -> {
                    listOfComponent.add(ProductBundlingDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.CONTENT_WIDGET -> {
                    listOfComponent.add(
                        ContentWidgetDataModel(
                            type = component.type,
                            name = component.componentName
                        )
                    )
                }
            }
        }
        return listOfComponent
    }

    /**
     * Combine all important data of all component to one Source of Truth
     * Component need to combine : Content , Notify Me , Media
     * @param DynamicProductInfoP1 Source of Truth PDP
     */
    fun mapToDynamicProductDetailP1(data: PdpGetLayout): DynamicProductInfoP1 {
        val contentData = data.components.find {
            it.type == ProductDetailConstant.PRODUCT_CONTENT
        }?.componentData?.firstOrNull()

        val mediaData = data.components.find {
            it.type == ProductDetailConstant.MEDIA
        }?.componentData?.firstOrNull() ?: ComponentData()

        val bestSellerComponent = mapToOneLinersComponent(ProductDetailConstant.BEST_SELLER, data)
        val stockAssuranceComponent = mapToOneLinersComponent(ProductDetailConstant.STOCK_ASSURANCE, data)

        val newDataWithMedia = contentData?.copy(media = mediaData.media, youtubeVideos = mediaData.youtubeVideos)
                ?: ComponentData()
        assignIdToMedia(newDataWithMedia.media)

        return DynamicProductInfoP1(
                layoutName = data.generalName,
                basic = data.basicInfo,
                data = newDataWithMedia,
                pdpSession = data.pdpSession,
                bestSellerContent = bestSellerComponent,
                stockAssuranceContent = stockAssuranceComponent
        )
    }

    private fun mapToOneLinersComponent(
            componentName: String,
            data: PdpGetLayout
    ): Map<String, OneLinersContent>? {
        return data.components.find {
            it.componentName == componentName
        }?.componentData?.map {
            OneLinersContent(
                    productID = it.productId,
                    content = it.oneLinerContent,
                    linkText = it.linkText,
                    color = it.color,
                    applink = it.applink,
                    separator = it.separator,
                    icon = it.icon,
                    isVisible = it.isVisible
            )
        }?.associateBy { it.productID }
    }

    private fun assignIdToMedia(listOfMedia: List<Media>) {
        listOfMedia.forEachIndexed { index, it ->
            it.id = (index + 1).toString()
        }
    }

    fun hashMapLayout(data: List<DynamicPdpDataModel>): MutableMap<String, DynamicPdpDataModel> {
        return data.associateBy({
            it.name()
        }, {
            it
        }).toMutableMap()
    }

    // Because the new variant data have several different type, we need to map this into the old one
    // the old variant data was from p2, but changed into p1 now
    fun mapVariantIntoOldDataClass(data: PdpGetLayout): ProductVariant? {
        val networkData = data.components.find {
            it.type == ProductDetailConstant.VARIANT
        }?.componentData?.firstOrNull() ?: return null

        return ProductVariant(
                parentId = networkData.parentId,
                errorCode = networkData.errorCode,
                sizeChart = networkData.sizeChart,
                defaultChild = networkData.defaultChild,
                variants = networkData.variants,
                children = networkData.children,
                maxFinalPrice = networkData.maxFinalPrice
        )
    }

    fun mapToWholesale(data: List<Wholesale>?): List<com.tokopedia.product.detail.common.data.model.product.Wholesale>? {
        return if (data == null || data.isEmpty()) {
            null
        } else {
            data.map {
                com.tokopedia.product.detail.common.data.model.product.Wholesale(it.minQty, it.price.value)
            }
        }
    }

    fun convertMediaToDataModel(media: MutableList<Media>): List<MediaDataModel> {
        return media.map {
            MediaDataModel(it.id, it.type, it.uRL300, it.uRLOriginal, it.uRLThumbnail, it.description, it.videoURLAndroid, it.isAutoplay)
        }
    }

    private fun mapToProductDetailInfoContent(data: ComponentData?): List<ProductDetailInfoContent> {
        if (data == null) return listOf()
        return data.content.map { ProductDetailInfoContent(icon = it.icon, title = it.title, subtitle = it.subtitle, applink = it.applink, showAtFront = it.showAtFront, isAnnotation = it.isAnnotation) }
    }

    private fun mapToCustomInfoUiModel(componentData: ComponentData?, componentName: String, componentType: String): ProductCustomInfoDataModel? {
        if (componentData == null) return null

        return ProductCustomInfoDataModel(
                name = componentName,
                type = componentType,
                title = componentData.title,
                applink = if (componentData.isApplink) componentData.applink else "",
                description = componentData.description,
                icon = componentData.icon,
                separator = componentData.separator)
    }

    fun generateProductReportFallback(productUrl: String): String {
        var fallbackUrl = productUrl
        if (!fallbackUrl.endsWith("/")) {
            fallbackUrl += "/"
        }
        fallbackUrl = fallbackUrl.replace("www.", "m.")
        fallbackUrl += "report/"
        return fallbackUrl
    }

    fun generateImageReviewUiData(data: ImageReviewGqlResponse.ProductReviewImageListQuery): ImageReview {
        val result = mutableListOf<ImageReviewItem>()

        data.detail?.images?.forEach {
            val review = data.detail?.reviews?.firstOrNull { review ->
                review.reviewId == it.reviewID
            } ?: return@forEach
            result.add(ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                    review.reviewer?.fullName, it.uriThumbnail,
                    it.uriLarge, review.rating, data.isHasNext, data.detail?.imageCountFmt, data.detail?.imageCount))
        }

        return ImageReview(result, data.detail?.imageCount ?: "")
    }

    fun generateProductInfoParcel(productInfoP1: DynamicProductInfoP1?, variantGuideLine: String, productInfoContent: List<ProductDetailInfoContent>, forceRefresh: Boolean): ProductInfoParcelData {
        val data = productInfoP1?.data
        val basic = productInfoP1?.basic
        return ProductInfoParcelData(basic?.productID ?: "", basic?.shopID
                ?: "", data?.name ?: "", data?.getProductImageUrl()
                ?: "", variantGuideLine, productInfoP1?.basic?.stats?.countTalk.toIntOrZero(), data?.youtubeVideos
                ?: listOf(), productInfoContent, forceRefresh, productInfoP1?.basic?.isTokoNow == true)
    }

    fun generateUserLocationRequest(localData: LocalCacheModel): UserLocationRequest {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return UserLocationRequest(
                localData.district_id.checkIfNumber("district_id"),
                localData.address_id.checkIfNumber("address_id"),
                localData.postal_code.checkIfNumber("postal_code"),
                latlong)
    }

    fun generateUserLocationRequestRates(localData: LocalCacheModel): String {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return "${localData.district_id}|${localData.postal_code}|${latlong}"
    }

    fun getAffiliateUIID(affiliateUniqueString: String, uuid: String): AffiliateUIIDRequest? {
        return if (affiliateUniqueString.isNotBlank()) AffiliateUIIDRequest(trackerID = uuid, uuid = affiliateUniqueString, irisSessionID = TrackApp.getInstance().gtm.irisSessionId) else null
    }

    fun determineSelectedOptionIds(variantData: ProductVariant,
                                   selectedChild: VariantChild?): MutableMap<String, String> {
        return AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(
                variantData,
                selectedChild?.optionIds
        )
    }

    fun generateProductShareData(productInfo: DynamicProductInfoP1, userId: String, shopUrl: String): ProductData {
        return ProductData(
                userId,
                productInfo.finalPrice.getCurrencyFormatted(),
                "${productInfo.data.isCashback.percentage}%",
                MethodChecker.fromHtml(productInfo.getProductName).toString(),
                productInfo.data.price.currency,
                productInfo.basic.url,
                shopUrl,
                productInfo.basic.shopName,
                productInfo.basic.productID,
                productInfo.data.getProductImageUrl() ?: ""
        )
    }

    fun generateAffiliateShareData(productInfo: DynamicProductInfoP1, shopInfo: ShopInfo?,
                                   variantData: ProductVariant?): AffiliatePDPInput {
        return AffiliatePDPInput(
                pageType= "pdp",
                product = Product(
                        productID = productInfo.basic.productID,
                        catLevel1 = productInfo.basic.category.detail.firstOrNull()?.id ?: "0",
                        catLevel2 = productInfo.basic.category.detail.getOrNull(1)?.id ?: "0",
                        catLevel3 = productInfo.basic.category.detail.getOrNull(2)?.id ?: "0",
                        productPrice = productInfo.data.price.value.toString(),
                        maxProductPrice = getMaxPriceVariant(productInfo, variantData).toString(), //to do
                        productStatus = productInfo.basic.status
                ),
                shop = Shop(
                        shopID = productInfo.basic.shopID,
                        isOS = productInfo.data.isOS,
                        isPM = productInfo.data.isPowerMerchant,
                        shopStatus = shopInfo?.statusInfo?.shopStatus
                )
        )
    }

    private fun getMaxPriceVariant(productInfo: DynamicProductInfoP1, variantData: ProductVariant?): Double {
        return if (productInfo.data.variant.isVariant && variantData != null) {
            variantData.maxFinalPrice.toDouble()
        } else {
            productInfo.finalPrice
        }
    }
}
