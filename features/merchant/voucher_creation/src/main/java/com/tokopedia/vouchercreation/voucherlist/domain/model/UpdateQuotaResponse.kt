package com.tokopedia.vouchercreation.voucherlist.domain.model

import com.google.gson.annotations.SerializedName

data class UpdateQuotaResponse (
        @SerializedName("merchantPromotionUpdateMVQuota")
        val updateVoucher: UpdateVoucherDataModel = UpdateVoucherDataModel()
)

