package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ProductData(
    @SerializedName("shipper_name")
    val shipperName: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_id")
    val shipperId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_product_id")
    val shipperProductId: Int = 0,

    @SerializedName("shipper_product_name")
    val shipperProductName: String = "",

    @SerializedName("shipper_product_desc")
    val shipperProductDesc: String = "",

    @SerializedName("shipper_weight")
    val shipperWeight: Int = 0,

    @SerializedName("is_show_map")
    val isShowMap: Int = 0,

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("recommend")
    var isRecommend: Boolean = false,

    @SerializedName("checksum")
    val checkSum: String = "",

    @SerializedName("ut")
    val unixTime: String = "",

    @SerializedName("promo_code")
    var promoCode: String = "",

    @SerializedName("ui_rates_hidden")
    val isUiRatesHidden: Boolean = false,

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: PriceData = PriceData(),

    @SerializedName("etd")
    val etd: EstimatedTimeDeliveryData = EstimatedTimeDeliveryData(),

    @SerializedName("insurance")
    val insurance: InsuranceData = InsuranceData(),

    @SerializedName("texts")
    val texts: ProductTextData = ProductTextData(),

    @SerializedName("error")
    val error: ErrorProductData = ErrorProductData(),

    @SerializedName("cod")
    val codProductData: CodProductData = CodProductData(),

    @SerializedName("features")
    val features: ProductFeatures = ProductFeatures(),

    @SerializedName("eta")
    val estimatedTimeArrival: EstimatedTimeArrival = EstimatedTimeArrival()
) : Parcelable
