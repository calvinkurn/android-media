package com.tokopedia.product.detail.data.util

import android.util.SparseArray
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.variant_common.model.*

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
                ProductDetailConstant.DISCUSSION -> {
                    listOfComponent.add(ProductDiscussionDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.DISCUSSION_FAQ -> {
                    listOfComponent.add(ProductDiscussionMostHelpfulDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_INFO -> {
                    listOfComponent.add(ProductInfoDataModel(type = component.type, name = component.componentName, data = mapToProductInfoContent(component.componentData)))
                }
                ProductDetailConstant.SHOP_INFO -> {
                    listOfComponent.add(ProductShopInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MINI_SOCIAL_PROOF -> {
                    listOfComponent.add(ProductMiniSocialProofDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MOST_HELPFUL_REVIEW -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.INFO -> {
                    val contentData = component.componentData.firstOrNull()
                    val content = if (contentData?.content?.isEmpty() == true) listOf(Content()) else contentData?.content
                    listOfComponent.add(ProductGeneralInfoDataModel(component.componentName, component.type, contentData?.applink
                            ?: "", contentData?.title ?: "",
                            contentData?.isApplink ?: true, contentData?.icon
                            ?: "", content ?: listOf(Content()))
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
                ProductDetailConstant.MINI_SHOP_INFO -> {
                    listOfComponent.add(ProductMiniShopInfoDataModel(type = component.type, name = component.componentName))
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

        val newDataWithMedia = contentData?.copy(media = mediaData.media, videos = mediaData.videos) ?: ComponentData()
        assignIdToMedia(newDataWithMedia.media)

        return DynamicProductInfoP1(layoutName = data.generalName, basic = data.basicInfo, data = newDataWithMedia, pdpSession = data.pdpSession)
    }

    private fun assignIdToMedia(listOfMedia: List<Media>){
        listOfMedia.forEachIndexed { index, it ->
            it.id = (index + 1).toString()
        }
    }

    fun hashMapLayout(data: List<DynamicPdpDataModel>): Map<String, DynamicPdpDataModel> {
        return data.associateBy({
            it.name()
        }, {
            it
        })
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
                        ?: "", thumbnail = data.picture?.thumbnail ?: ""))
            }

            Variant(pv = it.pv.toIntOrZero(),
                    v = it.v.toIntOrZero(),
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
                    ?: false, isCheckImei = newCampaignData?.isCheckImei, minOrder = newCampaignData?.minOrder, hideGimmick = newCampaignData?.hideGimmick)

            VariantChildCommon(productId = it.productId.toIntOrZero(), price = it.price, priceFmt = it.priceFmt, sku = it.sku, stock = stock,
                    optionIds = it.optionIds, name = it.name, url = it.url, picture = Picture(original = it.picture?.original, thumbnail = it.picture?.thumbnail),
                    campaign = campaign)
        }

        return ProductVariantCommon(
                parentId = networkData.parentId.toIntOrZero(),
                errorCode = networkData.errorCode,
                defaultChild = networkData.defaultChild.toIntOrZero(),
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
        return media.map { it ->
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
     */
    fun getTickerInfoData(tickerData: StickyLoginTickerPojo.TickerResponse): List<StickyLoginTickerPojo.TickerDetail> {
        return tickerData.response.tickers.filter {
            it.layout != StickyLoginConstant.LAYOUT_FLOATING
        }
    }

    fun generateImageReviewUiData(data: ImageReviewGqlResponse): List<ImageReviewItem> {
        val images = SparseArray<ImageReviewGqlResponse.Image>()
        val reviews = SparseArray<ImageReviewGqlResponse.Review>()
        val hasNext = data.productReviewImageListQuery?.isHasNext ?: false

        data.productReviewImageListQuery?.detail?.images?.forEach { images.put(it.imageAttachmentID, it) }
        data.productReviewImageListQuery?.detail?.reviews?.forEach { reviews.put(it.reviewId, it) }

        return data.productReviewImageListQuery?.list?.map {
            val image = images[it.imageID]
            val review = reviews[it.reviewID]
            ImageReviewItem(it.reviewID.toString(), review.timeFormat?.dateTimeFmt1,
                    review.reviewer?.fullName, image.uriThumbnail,
                    image.uriLarge, review.rating, hasNext, data.productReviewImageListQuery?.detail?.imageCount)
        } ?: listOf()
    }
}