package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by resakemal on 02/09/19.
 */
@Parcelize
class TopupBillsBanner(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("img_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("link_url")
        @Expose
        val linkUrl: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",
        @SerializedName("app_link")
        @Expose
        val applinkUrl: String = ""
) : Parcelable