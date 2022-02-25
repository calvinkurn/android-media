package com.tokopedia.vouchercreation.shop.detail.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.vouchercreation.common.model.MerchantVoucherModel

data class VoucherDetailResponse(
        @SerializedName("merchantPromotionGetMVDataByID")
        val result: VoucherDetailResult = VoucherDetailResult()
)

data class VoucherDetailResult(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val merchantVoucherModel: MerchantVoucherModel = MerchantVoucherModel()
)