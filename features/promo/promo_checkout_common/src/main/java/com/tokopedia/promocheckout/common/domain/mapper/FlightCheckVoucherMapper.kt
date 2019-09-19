package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import javax.inject.Inject

open class FlightCheckVoucherMapper @Inject constructor() {

    fun mapData(data: FlightCheckVoucher): DataUiModel {
        return DataUiModel(
                success = true,
                message = MessageUiModel("#ade3af", "green", data.message),
                codes = listOf(data.voucherCode),
                titleDescription = data.titleDescription,
                discountAmount = data.discountAmountPlain.toInt(),
                cashbackWalletAmount = data.cashbackAmountPlain.toInt(),
                isCoupon = data.isCoupon
        )
    }
}