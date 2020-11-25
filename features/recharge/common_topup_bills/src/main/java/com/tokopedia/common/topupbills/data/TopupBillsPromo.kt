package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 28/05/19.
 */
@Parcelize
class TopupBillsPromo(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("img_url")
        @Expose
        val urlBannerPromo: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",
        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",
        var voucherCodeCopied: Boolean = false
) : Parcelable