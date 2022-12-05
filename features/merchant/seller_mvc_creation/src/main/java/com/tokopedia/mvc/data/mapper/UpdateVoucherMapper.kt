package com.tokopedia.mvc.data.mapper

import androidx.annotation.StringDef
import com.tokopedia.mvc.data.request.UpdateVoucherRequest
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.util.constant.VoucherTypeConst
import javax.inject.Inject

class UpdateVoucherMapper @Inject constructor() {

    fun mapToDomain(response: UpdateVoucherResponse): UpdateVoucherResult {
        return UpdateVoucherResult(
            updateVoucherModel = response.updateVoucherModel.toDomainUpdateVoucherModel()
        )
    }

    fun UpdateVoucherResponse.UpdateVoucherModel.toDomainUpdateVoucherModel(): UpdateVoucherResult.UpdateVoucherModel {
        return UpdateVoucherResult.UpdateVoucherModel(
            status = this.status,
            message = this.message,
            processTime = this.processTime,
            data = this.data.toDomainUpdateVoucherData()
        )
    }

    fun UpdateVoucherResponse.UpdateVoucherModel.UpdateVoucherData.toDomainUpdateVoucherData():
        UpdateVoucherResult.UpdateVoucherModel.UpdateVoucherData {
        return UpdateVoucherResult.UpdateVoucherModel.UpdateVoucherData(
            redirectUrl = this.redirectUrl,
            voucherId = this.voucherId,
            status = this.status
        )
    }

    fun createRequestBody(
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): UpdateVoucherRequest {
        with(voucher) {
            return UpdateVoucherRequest(
                voucherId = id,
                benefitIdr = discountAmt,
                benefitMax = discountAmtMax,
                benefitPercent = discountAmt,
                benefitType = discountTypeFormatted,
                code = code,
                couponName = name,
                couponType = convertVoucherToCouponDefinition(type),
                dateStart = startDate,
                dateEnd = endDate,
                hourStart = startHour,
                hourEnd = endHour,
                image = image,
                imageSquare = imageSquare,
                isPublic = convertTargetType(isPublic),
                minPurchase = minimumAmt,
                quota = quota,
                token = token,
                source = "android-sellerapp"

            )
        }
    }

    private fun convertVoucherToCouponDefinition(@VoucherTypeConst type: Int): String {
        return when (type) {
            VoucherTypeConst.FREE_ONGKIR -> CouponType.SHIPPING
            VoucherTypeConst.DISCOUNT -> CouponType.DISCOUNT
            VoucherTypeConst.CASHBACK -> CouponType.CASHBACK
            else -> CouponType.SHIPPING
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(CouponType.SHIPPING, CouponType.CASHBACK, CouponType.DISCOUNT)
    annotation class CouponType {
        companion object {
            const val SHIPPING = "shipping"
            const val DISCOUNT = "discount"
            const val CASHBACK = "cashback"
        }
    }

    private fun convertTargetType(isPublic: Boolean): Int {
        return if (isPublic) {
            1
        } else {
            0
        }
    }
}
