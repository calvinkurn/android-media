package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by fajarnuha on 18/12/18.
 */
@Parcelize
data class CodData(
    @SerializedName("is_cod")
    val isCod: Int = 0,

    @SerializedName("cod_text")
    val codText: String = ""
) : Parcelable
