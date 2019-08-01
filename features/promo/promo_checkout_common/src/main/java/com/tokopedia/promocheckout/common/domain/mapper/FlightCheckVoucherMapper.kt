package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigitalData
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.common.domain.model.Message
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import javax.inject.Inject

open class FlightCheckVoucherMapper @Inject constructor() {

    fun mapData(data: FlightCheckVoucher): DataUiModel {
        return DataUiModel(
                success = true,
                message = MessageUiModel("green", "ACTIVE", data.message),
                codes = listOf(data.voucherCode),
                titleDescription = data.title,
                discountAmount = data.discountAmount.toInt(),
                cashbackWalletAmount = data.cashbackAmount.toInt(),
                isCoupon = data.isCoupon
        )
    }
}