package com.tokopedia.product.manage.common.feature.variant.presentation.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateCampaignVariantResult(
        @SerializedName("status")
        val status: ProductStatus,
        @SerializedName("stockCount")
        val stockCount: Int,
        @SerializedName("productName")
        val productName: String
): Parcelable