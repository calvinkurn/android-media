package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ServiceData(
    @SerializedName("service_name")
    val serviceName: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("service_id")
    val serviceId: Int = 0,

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("is_promo")
    var isPromo: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("range_price")
    val rangePrice: RangePriceData = RangePriceData(),

    @SerializedName("texts")
    val texts: ServiceTextData = ServiceTextData(),

    @SerializedName("features")
    val features: FeaturesData = FeaturesData(),

    @SerializedName("error")
    val error: ErrorServiceData = ErrorServiceData(),

    @SerializedName("products")
    val products: List<ProductData> = listOf(),

    @SerializedName("cod")
    val codData: CodData = CodData(),

    @SerializedName("order_priority")
    val orderPriority: OrderPriority = OrderPriority(),

    @SerializedName("mvc")
    val merchantVoucherData: MerchantVoucherData = MerchantVoucherData(),

    @SerializedName("ui_rates_hidden")
    var isUiRatesHidden: Boolean = false,

    @SuppressLint("Invalid Data Type")
    @SerializedName("selected_shipper_product_id")
    val selectedShipperProductId: Int = 0
) : Parcelable
