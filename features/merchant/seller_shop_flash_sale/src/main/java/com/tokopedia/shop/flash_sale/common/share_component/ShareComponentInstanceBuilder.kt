package com.tokopedia.shop.flash_sale.common.share_component

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.common.resource.ResourceProvider
import com.tokopedia.shop.flash_sale.domain.entity.CampaignBanner
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class ShareComponentInstanceBuilder @Inject constructor(
    private val userSession: UserSessionInterface,
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val UPCOMING_ID = 0
        private const val ONGOING_ID = 1
        private const val MAX_PRODUCT_DISPLAYED = 4
        private const val OVERLOAD_PRODUCT = 3

        private const val FIRST_PRODUCT = 0
        private const val SECOND_PRODUCT = 1
        private const val THIRD_PRODUCT = 2
        private const val FOURTH_PRODUCT = 4

        private const val TOTAL_PRODUCT_ONE = 1
        private const val TOTAL_PRODUCT_TWO = 2
        private const val TOTAL_PRODUCT_THREE = 3
        private const val TOTAL_PRODUCT_FOUR = 4

        private const val PAGE_NAME = "ShopFS"
        private const val THUMBNAIL_IMAGE = ""
    }


    data class Param(
        val shopName: String,
        val shopLogo: String,
        val shopBadge: String,
        val campaignStatusId: Int,
        val campaignId: Long,
        val isOngoing: Boolean,
        val startDate: Date,
        val endDate: Date,
        val totalProduct: Int,
        val products: List<CampaignBanner.Product>,
        val discount: Int,
    )

    fun build(
        param: Param,
        onShareOptionsClicked: (ShareModel) -> Unit,
        onCloseOptionClicked: () -> Unit
    ): UniversalShareBottomSheet {


        return UniversalShareBottomSheet.createInstance().apply {
            val listener = object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    onShareOptionsClicked(shareModel)
                }

                override fun onCloseOptionClicked() {
                    onCloseOptionClicked()
                }
            }

            init(listener)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.FLASH_SALE_TOKO)

            setMetaData(tnTitle = findTitle(param), tnImage = THUMBNAIL_IMAGE)
            setUtmCampaignData(
                pageName = PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = listOf(
                    userSession.shopId,
                    param.campaignStatusId.toString(),
                    param.campaignId.toString()
                ),
                feature = "share"
            )

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO,
                value = param.shopLogo
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME,
                value = param.shopName
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.BADGE,
                value = param.shopBadge //validateShopType(shopData.badge.Title) [official, pm, pro, none]
            )

            val date: String = if (param.isOngoing) {
                param.endDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
            } else {
                param.startDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
            }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.DATE,
                value = date
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.DISCOUNT,
                value = param.discount.toString()
            )

            val ongoingId = if (param.isOngoing) {
                ONGOING_ID
            } else {
                UPCOMING_ID
            }


            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.ONGOING,
                value = ongoingId.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_COUNT,
                value = param.totalProduct.toString()
            )

            val productOverload =
                if (param.totalProduct > MAX_PRODUCT_DISPLAYED) {
                    param.totalProduct - OVERLOAD_PRODUCT
                } else {
                    param.totalProduct
                }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_OVERLOAD,
                value = productOverload.toString()
            )

            if (param.totalProduct >= TOTAL_PRODUCT_ONE) {
                val product = param.products.getOrNull(FIRST_PRODUCT)

                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1,
                    value = product?.imageUrl.orEmpty()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_BEFORE,
                    value = product?.originalPrice.toString()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), param.isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_DISCOUNT,
                    value = product?.discountPercentage.toString()
                )
            }

            if (param.totalProduct >= TOTAL_PRODUCT_TWO) {
                val product = param.products.getOrNull(SECOND_PRODUCT)

                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2,
                    value = product?.imageUrl.orEmpty()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_BEFORE,
                    value = product?.originalPrice.toString()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), param.isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_DISCOUNT,
                    value = product?.discountPercentage.toString()
                )
            }

            if (param.totalProduct >= TOTAL_PRODUCT_THREE) {
                val product = param.products.getOrNull(THIRD_PRODUCT)

                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3,
                    value = product?.imageUrl.orEmpty()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_BEFORE,
                    value = product?.originalPrice.toString()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), param.isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_DISCOUNT,
                    value = product?.discountPercentage.toString()
                )
            }


            if (param.totalProduct >= TOTAL_PRODUCT_FOUR) {
                val product = param.products.getOrNull(FOURTH_PRODUCT)

                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4,
                    value = product?.imageUrl.orEmpty()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_BEFORE,
                    value = product?.originalPrice.toString()
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), param.isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_DISCOUNT,
                    value = product?.discountPercentage.toString()
                )
            }


        }
    }

    private fun formatDiscountPrice(discountedPrice: String, isOngoing: Boolean): String {
        return discountedPrice
        /*return if (isOngoing) {
            discountedPrice?.orEmpty().toString()
        } else {
            val formattedDiscountPrice = discountedPrice?.splitByThousand().orEmpty()
            if (formattedDiscountPrice.isNotEmpty()) {
                formattedDiscountPrice.replaceFirst(".", "?")
            } else {
                formattedDiscountPrice
            }
        }*/
    }

    private fun findTitle(param: Param): String {
        val formattedShopName = MethodChecker.fromHtml(param.shopName).toString()
        return if (param.isOngoing) {
            val formattedEndDate = param.endDate.formatTo(DateConstant.DATE)
            val formattedEndTime = param.endDate.formatTo(DateConstant.TIME_WIB)
            String.format(
                resourceProvider.getOngoingCampaignWording(),
                formattedShopName,
                formattedEndDate,
                formattedEndTime
            )
        } else {
            val formattedEndDate = param.startDate.formatTo(DateConstant.DATE)
            val formattedEndTime = param.startDate.formatTo(DateConstant.TIME_WIB)
            String.format(
                resourceProvider.getUpcomingCampaignWording(),
                formattedShopName,
                formattedEndDate,
                formattedEndTime
            )
        }
    }

    fun generateLinkerInstance(
        shopDomain: String,
        shopName: String,
        isOngoing: Boolean,
        shareModel: ShareModel,
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            id = "${userSession.shopId}?page_source=share"
            linkerData.type = LinkerData.SHOP_TYPE
            name = findOutgoingTitle(shopName)
            uri = "https://www.tokopedia.com/${shopDomain}?page_source=share"
            ogTitle = findOutgoingTitle(shopName)
            ogDescription = findOutgoingDescription(shopName, isOngoing)
            if (!TextUtils.isEmpty(shareModel.ogImgUrl)) {
                ogImageUrl = shareModel.ogImgUrl
            }
            isThrowOnError = true
        }
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun findOutgoingTitle(shopName: String): String {
        val formattedShopName = MethodChecker.fromHtml(shopName).toString()
        return String.format(resourceProvider.getOutgoingTitleWording(), formattedShopName)
    }

    fun findOutgoingDescription(shopName: String, isOngoing: Boolean): String {
        val formattedShopName = MethodChecker.fromHtml(shopName).toString()
        return if (isOngoing) {
            String.format(resourceProvider.getOutgoingOngoingCampaignWording(), formattedShopName)
        } else {
            String.format(
                resourceProvider.getOutgoingUpcomingCampaignWording(),
                formattedShopName
            )
        }
    }
}