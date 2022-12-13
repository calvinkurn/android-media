package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import java.util.*
import javax.inject.Inject

class UpdateVoucherMapper @Inject constructor() {

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
}

private val EMPTY = ""
fun Voucher.toUpdateVoucher(): UpdateVoucher {
    return UpdateVoucher(
        platform = "",
        isPublic = this.isPublic,
        voucherBenefitType = BenefitType.NOMINAL,
        voucherCashbackType = "idr",
        voucherCashbackPercentage = "20",
        voucherNominalAmount = 200,
        voucherNominalSymbol = "idr",
        voucherDiscountType = "a",
        voucherDiscountPercentage = "20",
        shopLogo = EMPTY,
        shopName = EMPTY,
        voucherCode = code,
        // TODO change
        voucherStartTime = Date(),
        voucherEndTime = Date(),
        productCount = 0,
        productImageUrls = emptyList(),
        audienceTarget = VoucherTargetBuyer.ALL_BUYER,
        // TODO change
        voucherType = PromoType.FREE_SHIPPING
    )
}
