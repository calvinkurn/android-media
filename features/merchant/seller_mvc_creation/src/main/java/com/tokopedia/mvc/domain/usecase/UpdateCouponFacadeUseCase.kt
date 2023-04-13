package com.tokopedia.mvc.domain.usecase

import com.tokopedia.campaign.utils.extension.toDate
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.utils.date.DateUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateCouponFacadeUseCase @Inject constructor(
    private val updateCouponUseCase: UpdateVoucherPeriodUseCase,
    private val initiateCouponUseCase: GetInitiateVoucherPageUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase
) {
    suspend fun execute(
        updateVoucher: UpdateVoucher,
        parentProductId: List<Long>,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): Boolean {
        return coroutineScope {
            val imageRatios = listOf(ImageRatio.HORIZONTAL, ImageRatio.SQUARE, ImageRatio.VERTICAL)
            val initiateVoucherDeferred = async { initiateCoupon() }
            val configuration = updateVoucher.mapToConfiguration()
            val generatedImagesDeferred = imageRatios.map {
                async {
                    getCouponImagePreviewUseCase.executeGetImageUrl(
                        isCreateMode = false,
                        voucherConfiguration = configuration,
                        parentProductId = parentProductId,
                        imageRatio = it
                    )
                }
            }

            val voucher = initiateVoucherDeferred.await()
            val imageUrl = generatedImagesDeferred.firstOrNull()?.await().orEmpty()
            val squareImageUrl = generatedImagesDeferred.getOrNull(SECOND_IMAGE_URL_INDEX)?.await().orEmpty()
            val portraitImageUrl = generatedImagesDeferred.getOrNull(THIRD_IMAGE_URL_INDEX)?.await().orEmpty()

            val updateCouponDeferred = async {
                updateCoupon(
                    updateVoucher,
                    voucher.token,
                    startDate,
                    startHour,
                    endDate,
                    endHour,
                    imageUrl,
                    squareImageUrl,
                    portraitImageUrl
                )
            }

            return@coroutineScope updateCouponDeferred.await()
        }
    }

    private fun UpdateVoucher.mapToConfiguration(): VoucherConfiguration {
        return VoucherConfiguration(
            voucherId = voucherId,
            isVoucherPublic = isPublic,
            benefitType = voucherBenefitType,
            voucherCode = voucherCode,
            startPeriod = startTime.toDate(DateUtil.YYYY_MM_DD),
            endPeriod = finishTime.toDate(DateUtil.YYYY_MM_DD),
            targetBuyer = audienceTarget,
            voucherName = voucherName,
            benefitIdr = discountAmt.toLong(),
            benefitMax = discountAmtMax.toLong(),
            promoType = PromoType.values().firstOrNull { value -> value.id == type }
                ?: PromoType.FREE_SHIPPING,
            minPurchase = minimumAmt.toLong(),
            quota = quota.toLong(),
            benefitPercent = discountPercentage,
            isVoucherProduct = productImageUrls.isNotEmpty()
        )
    }

    private suspend fun updateCoupon(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): Boolean {
        return updateCouponUseCase.updateVoucherPeriod(
            voucher, token, startDate, startHour, endDate, endHour, imageUrl, imageSquare, imagePortrait
        )
    }

    private suspend fun initiateCoupon(): VoucherCreationMetadata {
        return initiateCouponUseCase.execute()
    }

    companion object {
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
    }
}
