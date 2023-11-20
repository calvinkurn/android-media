package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPriority(
    @SerializedName("is_now")
    val now: Boolean = false,

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("formatted_price")
    val formattedPrice: String = "",

    @SerializedName("inactive_message")
    val inactiveMessage: String = "",

    @SerializedName("static_messages")
    val staticMessage: OrderPriorityStaticMessage = OrderPriorityStaticMessage()
) : Parcelable
