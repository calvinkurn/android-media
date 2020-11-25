package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedules(
        @SerializedName("schedule")
        @Expose
        var schedule: Schedule = Schedule(),

        @SerializedName("address_detail")
        @Expose
        var addressDetail: AddressDetail = AddressDetail(),

        @SerializedName("groups")
        @Expose
        var group: Group = Group()
) : Parcelable