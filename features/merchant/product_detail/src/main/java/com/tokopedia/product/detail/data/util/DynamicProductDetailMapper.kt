package com.tokopedia.product.detail.data.util

import android.os.Build
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.config.GlobalConfig
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirType
import com.tokopedia.product.detail.common.data.model.pdplayout.Component
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.OneLinersContent
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.Wholesale
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundling
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.LoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCategoryCarouselDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoTitleDataModel
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
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoSeeMore
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.asUiData
import com.tokopedia.product.detail.data.model.review.ReviewImage
import com.tokopedia.product.detail.data.util.ProductDetailConstant.GLOBAL_BUNDLING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_7
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_9_TOKONOW
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PRODUCT_BUNDLING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.SHOPADS_CAROUSEL
import com.tokopedia.product.detail.view.util.checkIfNumber
import com.tokopedia.product.share.ProductData
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.universal_sharing.model.BoTypeImageGeneratorParam
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.tracker.PageType
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
                    listOfComponent.add(mapToProductDetailInfo(component = component))
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
                    val customInfoData = mapToGeneralInfo(contentData, type = component.type, name = component.componentName)

                    customInfoData?.let {
                        listOfComponent.add(it)
                    }
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
                ProductDetailConstant.PRODUCT_LIST_VERTICAL -> {
                    listOfComponent.add(
                        ProductRecommendationVerticalPlaceholderDataModel(
                            type = component.type,
                            name = component.componentName
                        )
                    )
                    listOfComponent.add(LoadingDataModel())
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
                /**
                 * shipment_v2 use the same data model with shipment
                 */
                ProductDetailConstant.SHIPMENT_V2 -> {
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
                    // all data already provided in here (P1), so fill the data
                    val carouselData = component.componentData.firstOrNull()

                    if (carouselData?.categoryCarouselList?.isNotEmpty() == true) {
                        listOfComponent.add(
                            ProductCategoryCarouselDataModel(
                                type = component.type,
                                name = component.componentName,
                                titleCarousel = carouselData.titleCarousel,
                                linkText = carouselData.linkText,
                                applink = carouselData.applink,
                                categoryList = carouselData.categoryCarouselList
                            )
                        )
                    }
                }
                PRODUCT_BUNDLING -> {
                    if (component.componentName == GLOBAL_BUNDLING) {
                        val bundlingData = component.componentData.firstOrNull()
                        if (bundlingData != null) {
                            listOfComponent.add(
                                GlobalBundlingDataModel(
                                    type = component.type,
                                    name = component.componentName,
                                    data = generateGlobalBundlingData(bundlingData)
                                )
                            )
                        }
                    } else if (component.componentName == PRODUCT_BUNDLING) {
                        listOfComponent.add(
                            ProductBundlingDataModel(
                                type = component.type,
                                name = component.componentName
                            )
                        )
                    }
                }
                ProductDetailConstant.CONTENT_WIDGET -> {
                    listOfComponent.add(
                        ContentWidgetDataModel(
                            type = component.type,
                            name = component.componentName
                        )
                    )
                }
                ProductDetailConstant.AR_BUTTON -> {
                    listOfComponent.add(ArButtonDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.FINTECH_WIDGET_TYPE -> {
                    listOfComponent.add(
                        FintechWidgetDataModel(
                            type = component.type,
                            name = component.componentName
                        )
                    )
                }
                ProductDetailConstant.PRODUCT_SHOP_ADDITIONAL -> {
                    val shopAdditional = ProductShopAdditionalDataModel(
                        name = component.componentName,
                        type = component.type
                    )
                    listOfComponent.add(shopAdditional)
                }
                ProductDetailConstant.CUSTOM_INFO_TITLE -> {
                    val customInfoTitle = mapToCustomInfoTitle(component = component)

                    if (customInfoTitle != null) {
                        listOfComponent.add(customInfoTitle)
                    }
                }
            }
        }
        return listOfComponent
    }

    /**
     * mapping P1 General Info to Visitable Model
     */
    private fun mapToGeneralInfo(
        contentData: ComponentData?,
        type: String,
        name: String
    ): ProductGeneralInfoDataModel? {
        if (contentData == null) return null

        val content = contentData.content.ifEmpty { listOf(Content()) }

        return ProductGeneralInfoDataModel(
            name = name,
            type = type,
            applink = contentData.applink,
            title = contentData.title,
            isApplink = contentData.isApplink,
            parentIcon = contentData.icon,
            subtitle = content.firstOrNull()?.subtitle.orEmpty(),
            lightIcon = contentData.lightIcon,
            darkIcon = contentData.darkIcon,
            isPlaceholder = determinePlaceholder(name)
        )
    }

    /**
     * Use for General Info
     * component (name) should wait P2 data to render.
     */
    private fun determinePlaceholder(name: String): Boolean {
        return name == ProductDetailConstant.INFO_OBAT_KERAS
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

        val newDataWithMedia = contentData?.copy(
            media = mediaData.media,
            youtubeVideos = mediaData.youtubeVideos,
            containerType = mediaData.containerType
        ) ?: ComponentData()

        assignIdToMedia(newDataWithMedia.media)

        return DynamicProductInfoP1(
            layoutName = data.generalName,
            basic = data.basicInfo,
            data = newDataWithMedia,
            pdpSession = data.pdpSession,
            bestSellerContent = bestSellerComponent,
            stockAssuranceContent = stockAssuranceComponent,
            requestId = data.requestId
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
                isVisible = it.isVisible,
                eduLink = it.eduLink
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
            MediaDataModel(
                it.id,
                it.type,
                it.uRL300,
                it.uRLOriginal,
                it.uRLThumbnail,
                it.description,
                it.videoURLAndroid,
                it.isAutoplay,
                it.variantOptionId
            )
        }
    }

    private fun mapToProductDetailInfo(component: Component): ProductDetailInfoDataModel {
        val data = component.componentData.firstOrNull()
        val contents = mapToProductDetailInfoContent(data = data)
        val catalog = data?.catalogBottomSheet?.asUiData()
        val bottomSheet = data?.bottomSheet?.asUiData() ?: ProductDetailInfoSeeMore()

        return ProductDetailInfoDataModel(
            type = component.type,
            name = component.componentName,
            title = data?.title.orEmpty(),
            catalogBottomSheet = catalog,
            bottomSheet = bottomSheet,
            dataContent = contents
        )
    }

    private fun mapToProductDetailInfoContent(data: ComponentData?): List<ProductDetailInfoContent> {
        if (data == null) return listOf()
        return data.content.map {
            ProductDetailInfoContent(
                icon = it.icon,
                title = it.title,
                subtitle = it.subtitle,
                applink = it.applink,
                showAtFront = it.showAtFront,
                isAnnotation = it.isAnnotation,
                infoLink = it.infoLink,
                showAtBottomSheet = it.showAtBottomSheet
            )
        }
    }

    private fun mapToCustomInfoUiModel(
        componentData: ComponentData?,
        componentName: String,
        componentType: String
    ): ProductCustomInfoDataModel? {
        if (componentData == null) return null

        val label = componentData.labels.firstOrNull()
        return ProductCustomInfoDataModel(
            name = componentName,
            type = componentType,
            title = componentData.title,
            applink = if (componentData.isApplink) componentData.applink else "",
            description = componentData.description,
            icon = componentData.icon,
            separator = componentData.separator,
            labelColor = label?.color ?: "",
            labelValue = label?.value ?: "",
            lightIcon = componentData.lightIcon,
            darkIcon = componentData.darkIcon
        )
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

    fun generateImageReview(reviewImage: ImageReviewGqlResponse.ProductReviewImageListQuery): ReviewImage {
        return ReviewImage(
            buyerMediaCount = reviewImage.detail?.mediaCount.toIntOrZero(),
            reviewMediaThumbnails = generateReviewMediaThumbnails(reviewImage),
            staticSocialProofText = reviewImage.detail?.mediaTitle ?: ""
        )
    }

    private fun generateReviewMediaThumbnails(data: ImageReviewGqlResponse.ProductReviewImageListQuery): ReviewMediaThumbnailUiModel {
        val totalVideoToShow = data.detail?.videos?.size.orZero()
        val totalImageToShow = data.detail?.images?.size.orZero()
        val totalMediaToShow = totalVideoToShow + totalImageToShow
        val totalNotShowedMedia = data.detail?.mediaCount.toIntOrZero().minus(totalMediaToShow).coerceAtLeast(Int.ZERO)
        val mappedVideoThumbnails = data.detail?.videos?.mapIndexed { index, video ->
            val hasNext = data.isHasNext
            val lastItem = index == totalMediaToShow - 1
            if (lastItem && hasNext && totalNotShowedMedia.isMoreThanZero()) {
                ReviewMediaVideoThumbnailUiModel(
                    uiState = ReviewMediaVideoThumbnailUiState.ShowingSeeMore(
                        attachmentID = video.attachmentID,
                        reviewID = video.feedbackID.orEmpty(),
                        url = video.url.orEmpty(),
                        totalMediaCount = data.detail?.mediaCount.toIntOrZero(),
                        totalMediaCountFmt = data.detail?.mediaCountFmt.orEmpty()
                    )
                )
            } else {
                ReviewMediaVideoThumbnailUiModel(
                    uiState = ReviewMediaVideoThumbnailUiState.Showing(
                        attachmentID = video.attachmentID,
                        reviewID = video.feedbackID.orEmpty(),
                        url = video.url.orEmpty()
                    )
                )
            }
        }.orEmpty()
        val mappedImageThumbnails = data.detail?.images?.mapIndexed { index, image ->
            val hasNext = data.isHasNext
            val lastItem = index.plus(mappedVideoThumbnails.size) == totalMediaToShow - 1
            if (lastItem && hasNext && totalNotShowedMedia.isMoreThanZero()) {
                ReviewMediaImageThumbnailUiModel(
                    uiState = ReviewMediaImageThumbnailUiState.ShowingSeeMore(
                        attachmentID = image.imageAttachmentID,
                        reviewID = image.reviewID,
                        thumbnailUrl = image.uriThumbnail.orEmpty(),
                        fullSizeUrl = image.uriLarge.orEmpty(),
                        totalMediaCount = data.detail?.mediaCount.toIntOrZero(),
                        totalMediaCountFmt = data.detail?.mediaCountFmt.orEmpty()
                    )
                )
            } else {
                ReviewMediaImageThumbnailUiModel(
                    uiState = ReviewMediaImageThumbnailUiState.Showing(
                        attachmentID = image.imageAttachmentID,
                        reviewID = image.reviewID,
                        thumbnailUrl = image.uriThumbnail.orEmpty(),
                        fullSizeUrl = image.uriLarge.orEmpty()
                    )
                )
            }
        }.orEmpty()
        val mappedMediaThumbnails = mappedVideoThumbnails.plus(mappedImageThumbnails)
        return ReviewMediaThumbnailUiModel(mappedMediaThumbnails)
    }

    fun generateDetailedMediaResult(
        mediaThumbnails: ReviewMediaThumbnailUiModel?
    ): ProductrevGetReviewMedia {
        return ProductrevGetReviewMedia(
            reviewMedia = mediaThumbnails?.generateReviewMedia().orEmpty(),
            detail = Detail(
                reviewDetail = emptyList(),
                reviewGalleryImages = mediaThumbnails?.generateReviewGalleryImage().orEmpty(),
                reviewGalleryVideos = mediaThumbnails?.generateReviewGalleryVideo().orEmpty(),
                mediaCount = mediaThumbnails?.generateMediaCount().orZero()
            ),
            hasNext = mediaThumbnails?.isShowingSeeMore().orFalse()
        )
    }

    fun generateUserLocationRequest(localData: LocalCacheModel): UserLocationRequest {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return UserLocationRequest(
            districtID = localData.district_id.checkIfNumber("district_id"),
            addressID = localData.address_id.checkIfNumber("address_id"),
            postalCode = localData.postal_code.checkIfNumber("postal_code"),
            latlon = latlong,
            cityId = localData.city_id.checkIfNumber("city_id")
        )
    }

    fun generateTokoNowRequest(localData: LocalCacheModel): TokoNowParam {
        return TokoNowParam(
            shopId = localData.shop_id,
            warehouseId = localData.warehouse_id,
            serviceType = localData.service_type
        )
    }

    fun generateUserLocationRequestRates(localData: LocalCacheModel): String {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return "${localData.district_id}|${localData.postal_code}|$latlong"
    }

    fun determineSelectedOptionIds(
        variantData: ProductVariant,
        selectedChild: VariantChild?
    ): MutableMap<String, String> {
        return AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(
            variantData,
            selectedChild?.optionIds
        )
    }

    fun zeroIfEmpty(data: String?): String {
        return if (data == null || data.isEmpty()) {
            "0"
        } else {
            data
        }
    }

    fun generateProductShareData(productInfo: DynamicProductInfoP1, userId: String, shopUrl: String, bundleId: String): ProductData {
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
            productInfo.data.getProductImageUrl() ?: "",
            campaignId = zeroIfEmpty(productInfo.data.campaign.campaignID),
            bundleId = zeroIfEmpty(bundleId)
        )
    }

    fun generateAffiliateShareData(
        productInfo: DynamicProductInfoP1,
        shopInfo: ShopInfo?,
        variantData: ProductVariant?
    ): AffiliatePDPInput {
        return AffiliatePDPInput(
            pageType = PageType.PDP.value,
            product = Product(
                productID = productInfo.basic.productID,
                catLevel1 = productInfo.basic.category.detail.firstOrNull()?.id ?: "0",
                catLevel2 = productInfo.basic.category.detail.getOrNull(1)?.id ?: "0",
                catLevel3 = productInfo.basic.category.detail.getOrNull(2)?.id ?: "0",
                productPrice = productInfo.data.price.value.toString(),
                maxProductPrice = getMaxPriceVariant(productInfo, variantData).toString(), // to do
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

    fun generateImageGeneratorData(product: DynamicProductInfoP1, bebasOngkir: BebasOngkirImage): PdpParamModel {
        return PdpParamModel(
            productId = product.basic.productID,
            isBebasOngkir = isBebasOngkir(bebasOngkir.boType),
            bebasOngkirType = mapBebasOngkirType(bebasOngkir.boType),
            productPrice = getProductPrice(product),
            productRating = product.basic.stats.rating,
            productTitle = product.data.name,
            hasCampaign = product.data.campaign.activeAndHasId.compareTo(false).toString(),
            campaignName = product.data.campaign.campaignTypeName,
            campaignDiscount = product.data.campaign.percentageAmount.toInt(),
            newProductPrice = product.data.campaign.discountedPrice.toLong()

        )
    }

    private fun getProductPrice(product: DynamicProductInfoP1): Long {
        return if (product.data.campaign.activeAndHasId) {
            product.data.campaign.originalPrice.toLong()
        } else {
            product.data.price.value.toLong()
        }
    }

    private fun isBebasOngkir(type: Int) = type == BebasOngkirType.NON_BO.value

    private fun mapBebasOngkirType(type: Int): String {
        return when (type) {
            BebasOngkirType.BO_REGULER.value -> BoTypeImageGeneratorParam.BEBAS_ONGKIR.value
            BebasOngkirType.BO_EXTRA.value -> BoTypeImageGeneratorParam.BEBAS_ONGKIR_EXTRA.value
            else -> BoTypeImageGeneratorParam.NONE.value
        }
    }

    fun removeUnusedComponent(
        productInfo: DynamicProductInfoP1?,
        variantData: ProductVariant?,
        isShopOwner: Boolean,
        initialLayoutData: MutableList<DynamicPdpDataModel>
    ): MutableList<DynamicPdpDataModel> {
        val isTradein = productInfo?.data?.isTradeIn == true
        val isOfficialStore = productInfo?.data?.isOS == true
        val isVariant = productInfo?.isProductVariant() ?: false
        val isVariantEmpty = variantData == null || !variantData.hasChildren
        val higherThanLollipop = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1

        return initialLayoutData.filterNot {
            (it.name() == ProductDetailConstant.TRADE_IN && (!isTradein || isShopOwner)) ||
                (it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO) ||
                (it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO) ||
                (it.name() == ProductDetailConstant.VALUE_PROP && !isOfficialStore) ||
                (it.name() == ProductDetailConstant.VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) ||
                (it.name() == ProductDetailConstant.MINI_VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) ||
                (it.type() == ProductDetailConstant.PRODUCT_LIST && GlobalConfig.isSellerApp()) ||
                (it.name() == ProductDetailConstant.REPORT && (GlobalConfig.isSellerApp() || isShopOwner)) ||
                (it.name() == ProductDetailConstant.PLAY_CAROUSEL && GlobalConfig.isSellerApp()) ||
                /***
                 * remove palugada type with name
                 * (value_prop, wholesale, fullfilment, payment later install, order priority, cod)
                 */
                (it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO) ||
                (it.name() == ProductDetailConstant.PRODUCT_FULLFILMENT) ||
                (it.name() == ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO) ||
                (it.name() == ProductDetailConstant.ORDER_PRIORITY) ||
                /**
                 * Remove when lollipop and product of seller itself
                 */
                (
                    it.name() == ProductDetailConstant.AR_BUTTON &&
                        (GlobalConfig.isSellerApp() || !higherThanLollipop || isShopOwner)
                    )
        }.toMutableList()
    }

    private fun getMaxPriceVariant(productInfo: DynamicProductInfoP1, variantData: ProductVariant?): Double {
        return if (productInfo.data.variant.isVariant && variantData != null) {
            variantData.maxFinalPrice.toDouble()
        } else {
            productInfo.finalPrice
        }
    }

    fun getAffiliatePageDetail(productInfo: DynamicProductInfoP1): AffiliatePageDetail {
        val categoryId = productInfo.basic.category.detail.lastOrNull()?.id ?: ""
        return AffiliatePageDetail(
            pageId = productInfo.basic.productID,
            source = AffiliateSdkPageSource.PDP(
                shopId = productInfo.basic.shopID,
                productInfo = AffiliateSdkProductInfo(
                    categoryID = categoryId,
                    isVariant = productInfo.isProductVariant(),
                    stockQty = productInfo.getFinalStock().toIntOrZero()
                )
            )
        )
    }

    private fun generateGlobalBundlingData(bundlingData: ComponentData): GlobalBundling {
        return GlobalBundling(
            title = bundlingData.title,
            widgetType = bundlingData.widgetType,
            productId = bundlingData.productId,
            whId = bundlingData.whId
        )
    }

    private fun mapToCustomInfoTitle(component: Component): ProductCustomInfoTitleDataModel? {
        val data = component.componentData.firstOrNull() ?: return null

        return ProductCustomInfoTitleDataModel(
            name = component.componentName,
            type = component.type,
            title = data.title,
            status = ProductCustomInfoTitleDataModel.Status.fromString(data.status)
        )
    }
}
