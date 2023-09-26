package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductFeatures(
    @SerializedName("ontime_delivery_guarantee")
    val ontimeDeliveryGuarantee: OntimeDeliveryGuarantee = OntimeDeliveryGuarantee(),

    @SerializedName("mvc")
    val merchantVoucherProductData: MerchantVoucherProductData = MerchantVoucherProductData(),

    @SuppressLint("Invalid Data Type")
    @SerializedName("dynamic_price")
    val dynamicPriceData: DynamicPriceData = DynamicPriceData()
) : Parcelable
