package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
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
