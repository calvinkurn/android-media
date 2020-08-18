package com.tokopedia.product.detail.data.model.shop

import android.os.Parcelable
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory
import kotlinx.android.parcel.Parcelize

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Parcelize
data class ProductShopBBInfo(
        val name: String = "",
        val desc: String = "",
        val nameEN: String = "",
        val descEN: String = ""
): BlackBoxShipmentHolder(), Parcelable {

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

}