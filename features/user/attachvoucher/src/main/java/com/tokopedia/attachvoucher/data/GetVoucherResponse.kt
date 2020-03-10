package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class GetVoucherResponse(
        @SerializedName("getPublicMerchantVoucherList")
        val getPublicMerchantVoucherList: GetPublicMerchantVoucherList = GetPublicMerchantVoucherList()
) {
    val vouchers: List<VoucherUiModel> get() = getPublicMerchantVoucherList.vouchers
}