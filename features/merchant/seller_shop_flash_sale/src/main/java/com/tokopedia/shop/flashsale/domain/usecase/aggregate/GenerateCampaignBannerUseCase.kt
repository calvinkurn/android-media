package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.digitsOnly
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder.Companion.MAX_PRODUCT_DISPLAYED
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder.Companion.ONGOING_ID
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder.Companion.OVERLOAD_PRODUCT
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder.Companion.UPCOMING_ID
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_ONGOING
import com.tokopedia.shop.flashsale.domain.usecase.GenerateImageUseCase
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GenerateCampaignBannerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase,
    private val generateImageUseCase: GenerateImageUseCase
) : GraphqlUseCase<ShareComponentMetadata>(repository) {

    
    suspend fun execute(campaignId : Long): String {
        return coroutineScope {
            val shareComponentMetadataDeferred = async {
                getShareComponentMetadataUseCase.execute(campaignId)
            }
            val shareComponentMetadata = shareComponentMetadataDeferred.await()
            val imageUrlDeferred = async { generateImageUseCase.execute(populateImageGeneratorParams(shareComponentMetadata)) }


            val imageUrl = imageUrlDeferred.await()

            imageUrl
        }
    }
    
    private fun populateImageGeneratorParams(metaData: ShareComponentMetadata): Map<String, String> {
        val banner = metaData.banner
        val shop = metaData.shop
        val formattedShopName = MethodChecker.fromHtml(banner.shop.name).toString()
        val isOngoing = banner.campaignStatusId == CAMPAIGN_STATUS_ID_ONGOING
        val shopBadge = when {
            shop.isOfficial -> "official"
            shop.isPowerMerchant -> "pro"
            else -> "none"
        }

        val date: String = if (isOngoing) {
            banner.endDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
        } else {
            banner.startDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
        }

        val ongoingId = if (isOngoing) {
            ONGOING_ID
        } else {
            UPCOMING_ID
        }

        val productOverload =
            if (banner.products.size > MAX_PRODUCT_DISPLAYED) {
                banner.products.size - OVERLOAD_PRODUCT
            } else {
                banner.products.size
            }

        val params = mutableMapOf(
            ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO to banner.shop.logo,
            ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME to formattedShopName,
            ImageGeneratorConstants.ImageGeneratorKeys.BADGE to shopBadge,
            ImageGeneratorConstants.ImageGeneratorKeys.DATE to date,
            ImageGeneratorConstants.ImageGeneratorKeys.DISCOUNT to banner.maxDiscountPercentage.toString(),
            ImageGeneratorConstants.ImageGeneratorKeys.ONGOING to ongoingId.toString(),
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_COUNT to banner.products.size.toString(),
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_OVERLOAD to productOverload.toString(),
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL to banner.products.getOrNull(0)?.imageUrl.orEmpty(),
            ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM to "wa"
        )

        if (banner.products.size >= ShareComponentInstanceBuilder.TOTAL_PRODUCT_ONE) {
            val product = banner.products.getOrNull(ShareComponentInstanceBuilder.FIRST_PRODUCT)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1] = product?.imageUrl.orEmpty()
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_BEFORE] = formatOriginalPrice(product?.originalPrice.toString())
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_AFTER] = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_DISCOUNT] = product?.discountPercentage.toString()
        }

        if (banner.products.size >= ShareComponentInstanceBuilder.TOTAL_PRODUCT_TWO) {
            val product = banner.products.getOrNull(ShareComponentInstanceBuilder.SECOND_PRODUCT)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2] = product?.imageUrl.orEmpty()
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_BEFORE] = formatOriginalPrice(product?.originalPrice.toString())
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_AFTER] = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_DISCOUNT] = product?.discountPercentage.toString()
        }

        if (banner.products.size >= ShareComponentInstanceBuilder.TOTAL_PRODUCT_THREE) {
            val product = banner.products.getOrNull(ShareComponentInstanceBuilder.THIRD_PRODUCT)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3] = product?.imageUrl.orEmpty()
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_BEFORE] = formatOriginalPrice(product?.originalPrice.toString())
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_AFTER] = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_DISCOUNT] = product?.discountPercentage.toString()
        }


        if (banner.products.size >= ShareComponentInstanceBuilder.TOTAL_PRODUCT_FOUR) {
            val product = banner.products.getOrNull(ShareComponentInstanceBuilder.FOURTH_PRODUCT)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4] = product?.imageUrl.orEmpty()
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_BEFORE] = formatOriginalPrice(product?.originalPrice.toString())
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_AFTER] = formatDiscountPrice(product?.discountedPrice.orEmpty(), isOngoing)
            params[ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_DISCOUNT] = product?.discountPercentage.toString()
        }
        
        return params
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
        delimiter: String = ShareComponentInstanceBuilder.DELIMITER,
        discountedPrice: String
    ): String {
        try {
            val template = "%s${delimiter}%s"

            val splitResult = discountedPrice.split(delimiter)

            if (splitResult.isEmpty()) {
                return discountedPrice
            }

            val firstSegment = splitResult[ShareComponentInstanceBuilder.FIRST_SEGMENT]
            val firstSegmentDigitOnly = firstSegment.digitsOnly().toString()

            var replacement = Constant.EMPTY_STRING
            repeat(firstSegmentDigitOnly.length) {
                replacement += ShareComponentInstanceBuilder.QUESTION_MARK
            }

            val secondSegment = splitResult[ShareComponentInstanceBuilder.SECOND_SEGMENT]

            return String.format(template, replacement, secondSegment)
        } catch (e: Exception) {
            return discountedPrice
        }

    }

}