package com.tokopedia.shop.flashsale.common.share_component

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.digitsOnly
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.resource.ResourceProvider
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_ONGOING
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.Date
import javax.inject.Inject

class ShareComponentInstanceBuilder @Inject constructor(
    private val userSession: UserSessionInterface,
    private val resourceProvider: ResourceProvider,
    private val tracker: ShopFlashSaleTracker,
) {

    companion object {
        const val UPCOMING_ID = 0
        const val ONGOING_ID = 1
        const val MAX_PRODUCT_DISPLAYED = 4
        const val OVERLOAD_PRODUCT = 3

        const val FIRST_PRODUCT = 0
        const val SECOND_PRODUCT = 1
        const val THIRD_PRODUCT = 2
        const val FOURTH_PRODUCT = 3

        const val TOTAL_PRODUCT_ONE = 1
        const val TOTAL_PRODUCT_TWO = 2
        const val TOTAL_PRODUCT_THREE = 3
        const val TOTAL_PRODUCT_FOUR = 4

        private const val PAGE_NAME = "ShopFS"

        const val FIRST_SEGMENT = 0
        const val SECOND_SEGMENT = 1

        const val QUESTION_MARK = "?"
        const val DELIMITER = "."
    }

    fun build(
        thumbnailImageUrl : String,
        param: Param,
        onShareOptionClick: (ShareModel, LinkerShareResult, String) -> Unit,
        onCloseOptionClicked: () -> Unit
    ): UniversalShareBottomSheet {

        return UniversalShareBottomSheet.createInstance().apply {
            val isOngoing = param.campaignStatusId == CAMPAIGN_STATUS_ID_ONGOING

            val listener = object : ShareBottomsheetListener {

                override fun onShareOptionClicked(shareModel: ShareModel) {
                    tracker.sendClickShareEvent(shareModel.channel.orEmpty())
                    handleShareOptionSelection(param, isOngoing, shareModel, onShareOptionClick)
                }

                override fun onCloseOptionClicked() {
                    onCloseOptionClicked()
                }
            }

            init(listener)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.FS_TOKO)

            //tnTitle will be displayed on share bottomsheet
            setMetaData(tnTitle = findOutgoingDescription(param, isOngoing), tnImage = thumbnailImageUrl)
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

            val shopBadge = when {
                param.isOfficialStore -> "official"
                param.isPowerMerchant -> "pro"
                else -> "none"
            }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.BADGE,
                value = shopBadge
            )

            val date: String = if (isOngoing) {
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

            val ongoingId = if (isOngoing) {
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
                    value = formatOriginalPrice(product?.originalPrice.toString())
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_DISCOUNT,
                    value = product?.discountPercentage.orZero().toString()
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
                    value = formatOriginalPrice(product?.originalPrice.toString())
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_DISCOUNT,
                    value = product?.discountPercentage.orZero().toString()
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
                    value = formatOriginalPrice(product?.originalPrice.toString())
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
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
                    value = formatOriginalPrice(product?.originalPrice.toString())
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_AFTER,
                    value = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
                )
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_DISCOUNT,
                    value = product?.discountPercentage.toString()
                )
            }


        }
    }

    private fun formatOriginalPrice(originalPrice : String) : String {
        return originalPrice.digitsOnly().toString()
    }

    private fun formatDiscountPrice(discountedPrice: String, isOngoing: Boolean): String {
        if (isOngoing) {
            val discountedPriceNumber = discountedPrice.digitsOnly()
            return discountedPriceNumber.toString()
        }

        return maskDiscountedPrice(discountedPrice = discountedPrice)
    }

    private fun maskDiscountedPrice(
        delimiter: String = DELIMITER,
        discountedPrice: String
    ): String {
        try {
            val template = "%s${delimiter}%s"

            val splitResult = discountedPrice.split(delimiter)

            if (splitResult.isEmpty()) {
                return discountedPrice
            }

            val firstSegment = splitResult[FIRST_SEGMENT]
            val firstSegmentDigitOnly = firstSegment.digitsOnly().toString()

            var replacement = Constant.EMPTY_STRING
            repeat(firstSegmentDigitOnly.length) {
                replacement += QUESTION_MARK
            }

            val secondSegment = splitResult[SECOND_SEGMENT]

            return String.format(template, replacement, secondSegment)
        } catch (e: Exception) {
            return discountedPrice
        }

    }



    private fun handleShareOptionSelection(
        param: Param,
        isOngoing: Boolean,
        shareModel: ShareModel,
        onShareOptionClick: (ShareModel, LinkerShareResult, String) -> Unit
    ) {
        val linkerShareData: LinkerShareData = generateLinkerInstance(
            param,
            isOngoing,
            shareModel
        )

        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareResult: LinkerShareResult?) {
                val template = findOutgoingDescription(param, isOngoing)
                val outgoingText = "$template ${linkerShareResult?.shareUri.orEmpty()}"
                onShareOptionClick(shareModel, linkerShareResult ?: return, outgoingText)
            }

            override fun onError(linkerError: LinkerError?) {}
        }


        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                Constant.ZERO,
                linkerShareData,
                shareCallback
            )
        )

    }

    private fun generateLinkerInstance(
        param: Param,
        isOngoing: Boolean,
        shareModel: ShareModel
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            id = "${userSession.shopId}?page_source=share"
            linkerData.type = LinkerData.SHOP_TYPE
            uri = "https://www.tokopedia.com/${param.shopDomain}?page_source=share"
            ogTitle = findOutgoingTitle(param.shopName) //ogTitle will appear on the top of the description
            ogDescription = findOutgoingDescription(param, isOngoing) // ogDescription will appear as the main wording on the social media
            if (!TextUtils.isEmpty(shareModel.ogImgUrl)) {
                ogImageUrl = shareModel.ogImgUrl
            }
            isThrowOnError = true
        }
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun findOutgoingDescription(param: Param, isOngoing: Boolean): String {
        val formattedShopName = MethodChecker.fromHtml(param.shopName).toString()
        return if (isOngoing) {
            val formattedEndDate = param.endDate.formatTo(DateConstant.DATE)
            val formattedEndTime = param.endDate.formatTo(DateConstant.TIME_WIB)
            String.format(
                resourceProvider.getOutgoingOngoingDescription(),
                formattedShopName,
                formattedEndDate,
                formattedEndTime
            )
        } else {
            val formattedEndDate = param.startDate.formatTo(DateConstant.DATE)
            val formattedEndTime = param.startDate.formatTo(DateConstant.TIME_WIB)
            String.format(
                resourceProvider.getOutgoingUpcomingDescription(),
                formattedShopName,
                formattedEndDate,
                formattedEndTime
            )
        }
    }


    private fun findOutgoingTitle(shopName: String): String {
        val formattedShopName = MethodChecker.fromHtml(shopName).toString()
        return String.format(resourceProvider.getOutgoingTitleWording(), formattedShopName)
    }

    data class Param(
        val shopName: String,
        val shopLogo: String,
        val isPowerMerchant : Boolean,
        val isOfficialStore : Boolean,
        val shopDomain: String,
        val campaignStatusId: Int,
        val campaignId: Long,
        val startDate: Date,
        val endDate: Date,
        val totalProduct: Int,
        val products: List<CampaignBanner.Product>,
        val discount: Int,
    )

}