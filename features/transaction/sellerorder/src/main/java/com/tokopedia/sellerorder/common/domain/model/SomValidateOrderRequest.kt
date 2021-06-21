package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomValidateOrderRequest(@SerializedName("list_order_id")
                                         @Expose
                                         val orderIds: List<String>)