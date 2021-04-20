package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.android.parcel.Parcelize

data class UpdateCampaignStockResult(
        val productId: String,
        val productName: String,
        val stock: Int,
        val status: ProductStatus,
        val isSuccess: Boolean,
        val message: String? = null,
        val variantsMap: HashMap<String, UpdateCampaignVariantResult>? = null
)

@Parcelize
data class UpdateCampaignVariantResult(
        @SerializedName("status")
        val status: ProductStatus,
        @SerializedName("stockCount")
        val stockCount: Int,
        @SerializedName("productName")
        val productName: String
): Parcelable