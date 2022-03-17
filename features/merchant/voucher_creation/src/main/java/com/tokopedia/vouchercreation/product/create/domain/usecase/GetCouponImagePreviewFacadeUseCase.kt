package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.common.extension.getIndexAtOrEmpty
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

class GetCouponImagePreviewFacadeUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
    private val userSession: UserSessionInterface,
    private val remoteDataSource: ImageGeneratorRemoteDataSource
) {

    companion object {
        private const val EMPTY_STRING = ""
        private const val FIRST_IMAGE_URL = 0
        private const val SECOND_IMAGE_URL = 1
        private const val THIRD_IMAGE_URL = 2
        private const val THOUSAND  = 1_000f
        private const val MILLION = 1_000_000f
    }

    suspend fun execute(
        isCreateMode: Boolean,
        scope: CoroutineScope,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        parentProductId : List<Long>,
        imageRatio: ImageRatio
    ): ByteArray {
        val initiateCoupon = scope.async { initiateCoupon() }
        val topProductsDeferred = scope.async { getMostSoldProducts(parentProductId) }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }

        val shop = shopDeferred.await()
        val coupon = initiateCoupon.await()
        val topProducts = topProductsDeferred.await()

        val topProductImageUrls = topProducts.data.map { getImageUrlOrEmpty(it.pictures) }

        val generateImageDeferred = scope.async {
            generateImage(
                isCreateMode,
                coupon.voucherCodePrefix,
                couponInformation,
                couponSettings,
                topProductImageUrls,
                imageRatio,
                shop
            )
        }

        val image = generateImageDeferred.await()

        val imageByteArray = withContext(scope.coroutineContext) { image.bytes() }

        return imageByteArray
    }

    private suspend fun generateImage(
        isCreateMode: Boolean,
        couponCodePrefix: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        topProductImageUrls : List<String>,
        imageRatio: ImageRatio,
        shop: ShopBasicDataResult
    ): ResponseBody {
        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = when (couponInformation.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.PRIVATE -> "private"
            CouponInformation.Target.NOT_SELECTED -> EMPTY_STRING
        }

        val benefitType = when (couponSettings.type) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "gratis-ongkir"
        }

        val cashbackType = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> "percentage"
            else -> EMPTY_STRING
        }

        val symbol = when {
            couponSettings.discountAmount < THOUSAND -> "rb"
            couponSettings.discountAmount >= MILLION -> "jt"
            couponSettings.discountAmount >= THOUSAND -> "rb"
            else -> EMPTY_STRING
        }

        val amount = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> couponSettings.discountAmount
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> couponSettings.discountAmount
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> couponSettings.maxDiscount
            else -> couponSettings.discountAmount
        }

        val formattedDiscountAmount : Float = when {
            amount < THOUSAND -> amount.toFloat()
            amount >= MILLION -> (amount / MILLION)
            amount >= THOUSAND -> (amount / THOUSAND)
            else -> amount.toFloat()
        }

        val nominalAmount = formattedDiscountAmount.toInt()

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        val couponCode = if (isCreateMode && couponInformation.target == CouponInformation.Target.PRIVATE) {
            couponCodePrefix + couponInformation.code.uppercase()
        } else {
            couponInformation.code.uppercase()
        }

        val firstProductImageUrl = topProductImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL)
        val secondProductImageUrl = topProductImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL)
        val thirdProductImageUrl = topProductImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL)

        val secondProduct = if (secondProductImageUrl.isNotEmpty()) secondProductImageUrl else null
        val thirdProduct = if (thirdProductImageUrl.isNotEmpty()) thirdProductImageUrl else null

        val productCount = topProductImageUrls.filter { it.isNotBlank() }.size

        return remoteDataSource.previewImage(
            ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
            formattedImageRatio,
            couponVisibility,
            benefitType,
            cashbackType,
            couponSettings.discountPercentage,
            nominalAmount,
            symbol,
            shop.logo,
            shop.shopName,
            couponCode,
            startTime,
            endTime,
            productCount,
            firstProductImageUrl,
            secondProduct,
            thirdProduct,
            audienceTarget
        )
    }

    private suspend fun getMostSoldProducts(productIds: List<Long>): GetProductsByProductIdResponse.GetProductListData {
        getMostSoldProductsUseCase.params = GetMostSoldProductsUseCase.createParams(userSession.shopId, productIds)
        return getMostSoldProductsUseCase.executeOnBackground()
    }

    private suspend fun initiateCoupon(): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(isUpdate = true, isToCreateNewCoupon = false)
        return initiateCouponUseCase.executeOnBackground()
    }

    private fun getImageUrlOrEmpty(pictures : List<GetProductsByProductIdResponse.Picture>): String {
        if (pictures.isEmpty()) {
            return EMPTY_STRING
        }

        return pictures[0].urlThumbnail
    }
}