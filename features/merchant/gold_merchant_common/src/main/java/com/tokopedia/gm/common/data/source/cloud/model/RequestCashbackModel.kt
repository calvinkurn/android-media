package com.tokopedia.gm.common.data.source.cloud.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 10/2/17.
 */
data class RequestCashbackModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("product_id")
    @Expose
    private val product_id: Long,
    @SerializedName("cashback")
    @Expose
    private val cashback: Int
)