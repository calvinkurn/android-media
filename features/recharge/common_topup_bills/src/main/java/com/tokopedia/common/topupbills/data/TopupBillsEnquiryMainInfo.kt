package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 27/05/19.
 */
@Parcelize
data class TopupBillsEnquiryMainInfo(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
): Parcelable
