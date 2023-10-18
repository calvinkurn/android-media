package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import javax.inject.Inject

class UpdateVoucherMapper @Inject constructor() {

    private val EMPTY = ""

    fun map(response: UpdateVoucherResponse): UpdateVoucherResult {
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

    fun toUpdateVoucher(voucher: Voucher, dateStart: String = "", dateEnd: String = ""): UpdateVoucher {
        with(voucher) {
            return UpdateVoucher(
                platform = EMPTY,
                isPublic = isPublic,
                shopLogo = EMPTY,
                shopName = EMPTY,
                voucherCode = code,
                startTime = dateStart,
                finishTime = dateEnd,
                productImageUrls = emptyList(),
                audienceTarget = VoucherTargetBuyer.ALL_BUYER,
                type = type,
                voucherName = name,
                discountTypeFormatted = discountTypeFormatted,
                discountAmt = discountAmt,
                discountAmtMax = discountAmtMax,
                voucherId = id,
                minimumAmt = minimumAmt,
                quota = quota,
                discountPercentage = discountAmt
            )
        }
    }
}
