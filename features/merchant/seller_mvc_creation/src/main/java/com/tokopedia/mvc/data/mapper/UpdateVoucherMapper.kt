package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.request.UpdateVoucherRequest
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.util.constant.Source
import com.tokopedia.mvc.util.constant.TargetType
import com.tokopedia.mvc.util.constant.VoucherDefinition
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
                couponType = VoucherDefinition.convertVoucherToCouponDefinition(type),
                dateStart = startDate,
                dateEnd = endDate,
                hourStart = startHour,
                hourEnd = endHour,
                image = image,
                imageSquare = imageSquare,
                isPublic = TargetType.convertTargetType(isPublic),
                minPurchase = minimumAmt,
                quota = quota,
                token = token,
                source = Source.source
            )
        }
    }
}
