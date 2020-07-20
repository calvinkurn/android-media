package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/08/2019
 */
@Parcelize
class RefundDetailEntity(@SerializedName("top_info")
                         @Expose
                         val topInfo: List<KeyValueEntity> = arrayListOf(),
                         @SerializedName("middle_info")
                         @Expose
                         val middleInfo: List<CancellationTitleContentEntity> = arrayListOf(),
                         @SerializedName("bottom_info")
                         @Expose
                         val bottomInfo: List<KeyValueEntity> = arrayListOf(),
                         @SerializedName("note")
                         @Expose
                         val note: List<KeyValueEntity> = arrayListOf()) : Parcelable