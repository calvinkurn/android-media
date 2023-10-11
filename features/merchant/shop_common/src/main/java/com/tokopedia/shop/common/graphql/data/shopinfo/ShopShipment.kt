package com.tokopedia.shop.common.graphql.data.shopinfo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.model.ShopShipmentData
import com.tokopedia.shop.common.data.model.ShopShipmentData.*
import kotlinx.android.parcel.Parcelize

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Parcelize
data class ShopShipment(
    @SerializedName("isAvailable")
    @Expose
    val isAvailable: Int = 0,

    @SerializedName("code")
    @Expose
    val code: String = "",

    @SerializedName("shipmentID")
    @Expose
    val shipmentID: String = "",

    @SerializedName("image")
    @Expose
    val image: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("isPickup")
    @Expose
    val isPickup: Int = 0,

    @SerializedName("maxAddFee")
    @Expose
    val maxAddFee: Int = 0,

    @SerializedName("awbStatus")
    @Expose
    val awbStatus: Int = 0,

    @SerializedName("product")
    @Expose
    val product: List<ShipmentProduct> = listOf()
) : Parcelable {

    @Parcelize
    data class ShipmentProduct(
        @SerializedName("isAvailable")
        @Expose
        val isAvailable: Int = 0,

        @SerializedName("shipProdID")
        @Expose
        val shipProdID: String = "",

        @SerializedName("productName")
        @Expose
        val name: String = "",

        @SerializedName("uiHidden")
        @Expose
        val uiHidden: Boolean = true
    ) : Parcelable {

        fun mapToShipmentProductData(): ShipmentProductData {
            return ShipmentProductData(isAvailable, shipProdID, name, uiHidden)
        }
    }

    fun mapToShipmentData(): ShopShipmentData {
        val shipmentProductData = product.map {
            it.mapToShipmentProductData()
        }
        return ShopShipmentData(image, name, shipmentProductData)
    }
}
