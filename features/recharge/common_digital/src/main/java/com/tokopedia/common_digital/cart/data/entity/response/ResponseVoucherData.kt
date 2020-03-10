package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/7/17.
 */

class ResponseVoucherData {
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("attributes")
    @Expose
    var attributes: AttributesVoucher? = null
    @SerializedName("relationships")
    @Expose
    var relationships: RelationshipsVoucher? = null
}
