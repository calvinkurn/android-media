package com.tokopedia.notifications.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.PayloadExtraDataKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PayloadExtra(
    @SerializedName(PayloadExtraDataKey.CAMPAIGN_NAME)
    val campaignName : String? = null,

    @SerializedName(PayloadExtraDataKey.JOURNEY_ID)
    val journeyId : String? = null,

    @SerializedName(PayloadExtraDataKey.JOURNEY_NAME)
    val journeyName : String? = null,

) : Parcelable
