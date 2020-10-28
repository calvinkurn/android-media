package com.tokopedia.sellerorder.waitingpaymentorder.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

@Parcelize
data class WaitingPaymentOrderRequestParam(
        @SerializedName("is_mobile")
        @Expose
        val isMobile: Boolean = true,

        @SerializedName("next_payment_deadline")
        @Expose
        val nextPaymentDeadline: Long = 0,

        @SerializedName("lang")
        @Expose
        val lang: String = "id",

        @SerializedName("page")
        @Expose
        val page: Int = 1,

        @SerializedName("batch_page")
        @Expose
        val batchPage: Int = 1,

        @SerializedName("show_page")
        @Expose
        val showPage: Int = 1
): Parcelable