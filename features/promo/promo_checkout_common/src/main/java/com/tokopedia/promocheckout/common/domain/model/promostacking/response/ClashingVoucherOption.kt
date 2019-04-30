package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

data class ClashingVoucherOption(
        @SerializedName("voucher_orders")
        val voucherOrders: ArrayList<ClashingVoucherOrder> = ArrayList()
)