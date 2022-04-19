package com.tokopedia.product.detail.data.model.shop

import android.os.Parcelable
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import kotlinx.android.parcel.Parcelize

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Parcelize
data class ProductShopShipment (
        val isAvailable: Int = 0,
        val code: String = "",
        val shipmentID: String = "",
        val image: String = "",
        val name: String = "",
        val isPickup: Int = 0,
        val maxAddFee: Int = 0,
        val awbStatus: Int = 0,
        val product: List<ShopShipment.ShipmentProduct> = listOf()
): BlackBoxShipmentHolder(), Parcelable {

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

}