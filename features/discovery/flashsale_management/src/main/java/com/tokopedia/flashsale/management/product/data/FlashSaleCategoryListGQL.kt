
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
data class FlashSaleCategoryListGQL(
        @SerializedName("getCampaignInfo")
        @Expose val flashSaleCategoryListGQLData: FlashSaleCategoryListGQLData)

data class FlashSaleCategoryListGQLData(
        @SerializedName("data")
        @Expose val flashSaleCategoryListGQLContent: FlashSaleCategoryListGQLContent)

data class FlashSaleCategoryListGQLContent(
        @SerializedName("criteria")
        @Expose val criteriaList: List<Criteria>)
