package com.tokopedia.vouchercreation.voucherlist.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.vouchercreation.common.model.MerchantVoucherModel

/**
 * Created By @ilhamsuaib on 11/05/20
 */

data class GetMerchantVoucherListResponse(
        @Expose
        @SerializedName("MerchantPromotionGetMVList")
        val result: MerchantVoucherDataModel = MerchantVoucherDataModel()
)

data class MerchantVoucherDataModel(
        @Expose
        @SerializedName("data")
        val data: MerchantVoucherListDataModel = MerchantVoucherListDataModel()
)

data class MerchantVoucherListDataModel(
        @Expose
        @SerializedName("vouchers")
        val vouchers: List<MerchantVoucherModel> = emptyList()
)