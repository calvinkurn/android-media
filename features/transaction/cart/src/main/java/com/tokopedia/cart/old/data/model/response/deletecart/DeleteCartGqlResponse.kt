package com.tokopedia.cart.old.data.model.response.deletecart

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class DeleteCartGqlResponse(
        @SerializedName("remove_from_cart")
        val deleteCartDataResponse: DeleteCartDataResponse
)