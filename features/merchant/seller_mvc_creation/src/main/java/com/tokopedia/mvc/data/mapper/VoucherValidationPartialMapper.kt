package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import javax.inject.Inject

class VoucherValidationPartialMapper @Inject constructor() {

    fun map(response: VoucherValidationPartialResponse): VoucherValidationResult {
        val data = response.voucherValidationPartial.data
        return VoucherValidationResult(
            data.totalAvailableQuota,
            data.toValidationDate(),
            data.toValidationError(),
            data.toValidationProduct()
        )
    }

    private fun VoucherValidationPartialResponse.VoucherValidationPartial.Data.toValidationDate(): List<VoucherValidationResult.ValidationDate> {
        return this.validationDate.map {
            VoucherValidationResult.ValidationDate(
                it.endDate,
                it.startDate,
                it.endHour,
                it.startHour,
                it.totalLiveTime,
                it.available,
                it.notAvailableReason,
                it.type
            )
        }
    }

    private fun VoucherValidationPartialResponse.VoucherValidationPartial.Data.toValidationError(): VoucherValidationResult.ValidationError {
        return VoucherValidationResult.ValidationError(
            this.validationError.benefitIdr,
            this.validationError.benefitMax,
            this.validationError.benefitPercent,
            this.validationError.benefitType,
            this.validationError.code,
            this.validationError.couponName,
            this.validationError.couponType,
            this.validationError.dateEnd,
            this.validationError.dateStart,
            this.validationError.hourEnd,
            this.validationError.hourStart,
            this.validationError.image,
            this.validationError.imageSquare,
            this.validationError.isPublic,
            this.validationError.minPurchase,
            this.validationError.minPurchaseType,
            this.validationError.minimumTierLevel,
            this.validationError.quota
        )
    }

    private fun VoucherValidationPartialResponse.VoucherValidationPartial.Data.toValidationProduct(): List<VoucherValidationResult.ValidationProduct> {
        return this.validationProduct.map {
            val variants = it.variant.map {
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    it.productId,
                    it.productName,
                    it.price.toLongOrZero(),
                    it.stock,
                    it.isEligible,
                    it.reason
                )
            }

            VoucherValidationResult.ValidationProduct(
                it.isEligible,
                it.isVariant,
                it.parentProductId,
                it.reason,
                variants
            )
        }
    }
}
