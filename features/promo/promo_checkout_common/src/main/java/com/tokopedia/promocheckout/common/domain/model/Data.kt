package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("error")
    @Expose
    var error: String = ""
    @SerializedName("data_voucher")
    @Expose
    var dataVoucher: DataVoucher = DataVoucher()

}
