package com.tokopedia.flashsale.management.product.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleTncGQL(
        @SerializedName("getCampaignInfo")
        @Expose val flashSaleTncGQLData: FlashSaleTncGQLData)

data class FlashSaleTncGQLData(
        @SerializedName("data")
        @Expose val flashSaleTncContent: FlashSaleTncContent)

data class FlashSaleTncContent(
        @SerializedName("tnc")
        @Expose val tnc: String,

        @SerializedName("tnc_last_updated")
        @Expose val tncLastUpdated: String = "",

        @SerializedName("status_id")
        @Expose val statusId: Int,

        @SerializedName("status_info")
        @Expose val statusInfo: StatusInfo
)

data class StatusInfo(
        @SerializedName("status_name")
        @Expose val name: String = "",

        @SerializedName("status_label")
        @Expose val label: String = "")
