package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.variant_common.model.ProductVariantCommon

object DynamicProductDetailMapper {

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
                    listOfComponent.add(ProductInfoDataModel(mapToProductInfoContent(component.componentData), type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SHOP_INFO -> {
                    listOfComponent.add(ProductShopInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SOCIAL_PROOF -> {
                    if (component.componentName == ProductDetailConstant.SOCIAL_PROOF_PV) {
                        listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName, isSocialProofPv = true))
                    } else {
                        listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName, isSocialProofPv = false))
                    }
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
                ProductDetailConstant.VALUE_PROPOSITION -> {
                    listOfComponent.add(ProductValuePropositionDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.VARIANT -> {
                    listOfComponent.add(VariantDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.MINI_SHOP_INFO -> {
                    listOfComponent.add(ProductMiniShopInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_CONTENT -> {
                    //Will be remove soon
                    if (component.componentName == ProductDetailConstant.PRODUCT_CONTENT) {
                        listOfComponent.add(ProductContentDataModel(type = component.type, name = component.componentName))
                    } else {
                        listOfComponent.add(ProductSnapshotDataModel(type = component.type, name = component.componentName))
                    }
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
            }
        }
        return listOfComponent
    }

    /**
     * Combine all important data of all component to one Source of Truth
     * Component need to combine : Snapshot/Content , Notify Me , Media
     * @param DynamicProductInfoP1 Source of Truth PDP
     */
    fun mapToDynamicProductDetailP1(data: PdpGetLayout): DynamicProductInfoP1 {
        val contentData = data.components.find {
            it.type == ProductDetailConstant.PRODUCT_CONTENT
        }?.componentData?.firstOrNull()

        val upcomingData = data.components.find {
            it.type == ProductDetailConstant.NOTIFY_ME
        }?.componentData?.firstOrNull() ?: ComponentData()

        val mediaData = data.components.find {
            it.type == ProductDetailConstant.MEDIA
        }?.componentData?.firstOrNull() ?: ComponentData()

        val newDataWithUpcoming = contentData?.copy(
                campaignId = upcomingData.campaignId,
                campaignType = upcomingData.campaignType,
                campaignTypeName = upcomingData.campaignTypeName,
                endDate = upcomingData.endDate,
                startDate = upcomingData.startDate,
                notifyMe = upcomingData.notifyMe
        ) ?: ComponentData()

        val newDataWithMedia = newDataWithUpcoming.copy(media = mediaData.media, videos = mediaData.videos)
        assignIdToMedia(newDataWithMedia.media)

        return DynamicProductInfoP1(layoutName = data.generalName, basic = data.basicInfo, data = newDataWithMedia)
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

    fun generateCartTypeVariantParams(dynamicProductInfoP1: DynamicProductInfoP1?, productVariant: ProductVariantCommon?): List<CartRedirectionParams> {

        return productVariant?.children?.map {
            val listOfFlags = mutableListOf<String>()
            if (dynamicProductInfoP1?.data?.preOrder?.isActive == true) listOfFlags.add(ProductDetailConstant.KEY_PREORDER)
            if (dynamicProductInfoP1?.basic?.isLeasing == true) listOfFlags.add(ProductDetailConstant.KEY_LEASING)
            if (it.campaign?.isUsingOvo == true) listOfFlags.add(ProductDetailConstant.KEY_OVO_DEALS)

            CartRedirectionParams(it.campaign?.campaignID?.toIntOrNull() ?: 0,
                    it.campaign?.campaignType ?: 0, listOfFlags)
        } ?: listOf()
    }

    fun generateCartTypeParam(dynamicProductInfoP1: DynamicProductInfoP1?): List<CartRedirectionParams> {
        val campaignId = dynamicProductInfoP1?.data?.campaign?.campaignID?.toIntOrNull() ?: 0
        val campaignTypeId = dynamicProductInfoP1?.data?.campaign?.campaignType?.toIntOrNull() ?: 0
        val listOfFlags = mutableListOf<String>()
        if (dynamicProductInfoP1?.data?.preOrder?.isActive == true) listOfFlags.add(ProductDetailConstant.KEY_PREORDER)
        if (dynamicProductInfoP1?.basic?.isLeasing == true) listOfFlags.add(ProductDetailConstant.KEY_LEASING)
        if (dynamicProductInfoP1?.data?.campaign?.isUsingOvo == true) listOfFlags.add(ProductDetailConstant.KEY_OVO_DEALS)

        return listOf(CartRedirectionParams(campaignId, campaignTypeId, listOfFlags))
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
}