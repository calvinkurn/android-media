package com.tokopedia.vouchercreation.common.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.UpdateVoucherDataModel

data class CancelVoucherResponse (
        @SerializedName("merchantPromotionUpdateStatusMV")
        val cancelVoucher: UpdateVoucherDataModel = UpdateVoucherDataModel()
)