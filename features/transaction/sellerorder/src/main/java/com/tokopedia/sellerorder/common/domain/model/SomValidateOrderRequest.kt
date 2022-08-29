package com.tokopedia.sellerorder.common.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomValidateOrderRequest(@SuppressLint("Invalid Data Type")
                                   @SerializedName("list_order_id")
                                   @Expose
                                   val orderIds: List<String>)