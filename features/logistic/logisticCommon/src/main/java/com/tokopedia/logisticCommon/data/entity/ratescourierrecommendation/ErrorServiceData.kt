package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 11/10/18.
 */
@Parcelize
data class ErrorServiceData(
    @SerializedName("error_id")
    val errorId: String = "",

    @SerializedName("error_message")
    val errorMessage: String = ""
) : Parcelable
