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
class CancellationTitleContentEntity(@SerializedName("title")
                                     @Expose
                                     val title: String = "",
                                     @SerializedName("content")
                                     @Expose
                                     val content: List<KeyValueEntity> = arrayListOf()) : Parcelable