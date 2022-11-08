package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CPLParam(
    @SerializedName("input")
    val cplDataParam: CPLDataParam = CPLDataParam()
) : GqlParam {
    data class CPLDataParam(
        @SerializedName("shop_id")
        val shopId: Long = 0L,
        @SerializedName("product_id")
        val productId: Long? = null,
        @SerializedName("product_cpls")
        val productCpls: String? = null,
        @SerializedName("source")
        val source: String = "android",
    ) : GqlParam
}
