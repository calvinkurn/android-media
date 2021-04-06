package com.tokopedia.product.detail.data.util

import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.data.model.ratesestimate.UserLocationRequest
import com.tokopedia.product.detail.data.model.review.ImageReview
import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.LAYOUT_FLOATING
import com.tokopedia.variant_common.model.*
import com.tokopedia.variant_common.model.ThematicCampaign

object DynamicProductDetailMapper {

    /**
     * Map network data into UI data by type, just assign type and name here. The data will be assigned in fragment
     * except info type
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
                ProductDetailConstant.PRODUCT_INFO -> {
                    listOfComponent.add(ProductInfoDataModel(type = component.type, name = component.componentName, data = mapToProductInfoContent(component.componentData)))
                }
                ProductDetailConstant.PRODUCT_DETAIL -> {
                    listOfComponent.add(ProductDetailInfoDataModel(type = component.type, name = component.componentName, dataContent = mapToProductDetailInfoContent(component.componentData.firstOrNull())))
                }
                ProductDetailConstant.MINI_SOCIAL_PROOF -> {
                    listOfComponent.add(ProductMiniSocialProofDataModel(type = component.type, name = component.componentName))
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
                ProductDetailConstant.PRODUCT_LIST -> {
                    listOfComponent.add(ProductRecommendationDataModel(type = component.type, name = component.componentName, position = index))
                }
                ProductDetailConstant.SHOP_VOUCHER -> {
                    listOfComponent.add(ProductMerchantVoucherDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.VARIANT -> {
                    listOfComponent.add(VariantDataModel(type = component.type, name = component.componentName))
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

        val newDataWithMedia = contentData?.copy(media = mediaData.media, youtubeVideos = mediaData.youtubeVideos)
                ?: ComponentData()
        assignIdToMedia(newDataWithMedia.media)

        return DynamicProductInfoP1(layoutName = data.generalName, basic = data.basicInfo, data = newDataWithMedia, pdpSession = data.pdpSession)
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
    fun mapVariantIntoOldDataClass(data: PdpGetLayout): ProductVariantCommon? {
        val networkData = data.components.find {
            it.type == ProductDetailConstant.VARIANT
        }?.componentData?.firstOrNull() ?: return null

        val variants = networkData.variants.map { it ->
            val newOption = it.options.map { data ->
                Option(id = data.id.toIntOrZero(), vuv = data.vuv.toIntOrZero(), value = data.value, hex = data.hex, picture = Picture(original = data.picture?.original
                        ?: "", thumbnail = data.picture?.thumbnail ?: "", url100 = data.picture?.url100 ?: ""))
            }

            Variant(pv = it.pv,
                    v = it.v,
                    name = it.name,
                    identifier = it.identifier,
                    options = newOption)
        }

        val child = networkData.children.map {
            val stock = VariantStock(stock = it.stock?.stock.toIntOrZero(), isBuyable = it.stock?.isBuyable, stockWording = it.stock?.stockWording,
                    stockWordingHTML = it.stock?.stockWordingHTML, minimumOrder = it.stock?.minimumOrder.toIntOrZero())

            val newCampaignData = it.campaign
            val campaign = Campaign(campaignID = newCampaignData?.campaignID, isActive = newCampaignData?.isActive, originalPrice = newCampaignData?.originalPrice,
                    originalPriceFmt = newCampaignData?.originalPriceFmt, discountedPercentage = newCampaignData?.discountedPercentage, discountedPrice = newCampaignData?.discountedPrice,
                    campaignType = newCampaignData?.campaignType.toIntOrZero(), campaignTypeName = newCampaignData?.campaignTypeName,
                    startDate = newCampaignData?.startDate, endDateUnix = newCampaignData?.endDateUnix, stock = newCampaignData?.stock, isAppsOnly = newCampaignData?.isAppsOnly, applinks = newCampaignData?.applinks,
                    stockSoldPercentage = newCampaignData?.stockSoldPercentage, isUsingOvo = newCampaignData?.isUsingOvo
                    ?: false, isCheckImei = newCampaignData?.isCheckImei, minOrder = newCampaignData?.minOrder, hideGimmick = newCampaignData?.hideGimmick,
                    background = newCampaignData?.background ?: "", campaignIdentifier = newCampaignData?.campaignIdentifier ?: 0)

            val thematicCampaignData = it.thematicCampaign
            val thematicCampaign = ThematicCampaign(
                    campaignName = thematicCampaignData?.campaignName,
                    icon = thematicCampaignData?.icon,
                    background = thematicCampaignData?.background,
                    additionalInfo = thematicCampaignData?.additionalInfo)

            VariantChildCommon(productId = it.productId, price = it.price, priceFmt = it.priceFmt, sku = it.sku, stock = stock,
                    optionIds = it.optionIds, name = it.name, url = it.url, picture = Picture(original = it.picture?.original, thumbnail = it.picture?.thumbnail),
                    campaign = campaign, thematicCampaign = thematicCampaign,isCod = it.isCod)
        }

        return ProductVariantCommon(
                parentId = networkData.parentId,
                errorCode = networkData.errorCode,
                defaultChild = networkData.defaultChild,
                sizeChart = networkData.sizeChart,
                variant = variants,
                children = child
        )
    }

    fun generateButtonAction(it: String, atcButton: Boolean, leasing: Boolean): Int {
        return when {
            atcButton -> ProductDetailConstant.ATC_BUTTON
            leasing -> ProductDetailConstant.LEASING_BUTTON
            it == ProductDetailConstant.KEY_NORMAL_BUTTON -> {
                ProductDetailConstant.BUY_BUTTON
            }
            it == ProductDetailConstant.KEY_OCS_BUTTON -> {
                ProductDetailConstant.OCS_BUTTON
            }
            it == ProductDetailConstant.KEY_OCC_BUTTON -> {
                ProductDetailConstant.OCC_BUTTON
            }
            it == ProductDetailConstant.KEY_REMIND_ME -> {
                ProductDetailConstant.REMIND_ME_BUTTON
            }
            it == ProductDetailConstant.KEY_CHECK_WISHLIST -> {
                ProductDetailConstant.CHECK_WISHLIST_BUTTON
            }
            else -> ProductDetailConstant.BUY_BUTTON
        }
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

    fun convertMediaToDataModel(media: MutableList<Media>): List<MediaDataModel> {
        return media.map {
            MediaDataModel(it.id, it.type, it.uRL300, it.uRLOriginal, it.uRLThumbnail, it.description, it.videoURLAndroid, it.isAutoplay)
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

    /**
     * Ticker is used for show general message like : corona, shipping delay,  etc
     * since we are using the same GQL as sticky login, we don't want sticky login item so we remove this
     * LAYOUT_FLOATING should be sticky login
     * *
     * update : now it's not used class from sticky login module anymore
     */
    fun getTickerInfoData(tickerData: GeneralTickerDataModel.TickerResponse): List<GeneralTickerDataModel.TickerDetailDataModel> {
        return tickerData.response.tickerDataModels.filter {
            it.layout != LAYOUT_FLOATING
        }
    }

    fun generateImageReviewUiData(data: ImageReviewGqlResponse.ProductReviewImageListQuery): ImageReview {
        val result = mutableListOf<ImageReviewItem>()

        data.detail?.images?.forEach {
            val review = data.detail?.reviews?.firstOrNull { review ->
                review.reviewId == it.reviewID
            } ?: return@forEach
            result.add(ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                    review.reviewer?.fullName, it.uriThumbnail,
                    it.uriLarge, review.rating, data.isHasNext, data.detail?.imageCountFmt))
        }

        return ImageReview(result, data.detail?.imageCount ?: "")
    }

    fun generateProductInfoParcel(productInfoP1: DynamicProductInfoP1?, variantGuideLine: String, productInfoContent: List<ProductDetailInfoContent>, forceRefresh: Boolean): ProductInfoParcelData {
        val data = productInfoP1?.data
        val basic = productInfoP1?.basic
        return ProductInfoParcelData(basic?.productID ?: "", basic?.shopID
                ?: "", data?.name ?: "", data?.getProductImageUrl()
                ?: "", variantGuideLine, productInfoP1?.basic?.stats?.countTalk.toIntOrZero(), data?.youtubeVideos
                ?: listOf(), productInfoContent, forceRefresh)
    }

    fun generateUserLocationRequest(localData: LocalCacheModel): UserLocationRequest {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return UserLocationRequest(
                localData.district_id,
                localData.address_id,
                localData.postal_code,
                latlong)
    }

    fun generateUserLocationRequestRates(localData: LocalCacheModel): String {
        val latlong = if (localData.lat.isEmpty() && localData.long.isEmpty()) "" else "${localData.lat},${localData.long}"
        return "${localData.district_id}|${localData.postal_code}|${latlong}"
    }
}