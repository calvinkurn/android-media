package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class GetPublicMerchantVoucherList(
        @SerializedName("vouchers")
        val vouchers: List<VoucherUiModel> = listOf()
)