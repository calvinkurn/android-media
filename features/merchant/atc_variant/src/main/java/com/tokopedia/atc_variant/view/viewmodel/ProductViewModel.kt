package com.tokopedia.atc_variant.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 30/11/18.
 */
@Parcelize
data class ProductViewModel(
        var parentId: Int = 0,
        var productName: String = "",
        var productPrice: Int = 0,
        var productImageUrl: String = "",
        var productChildrenList: ArrayList<ProductChild> = arrayListOf(),
        var selectedVariantOptionsIdMap: LinkedHashMap<Int, Int> = LinkedHashMap(),
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var originalPrice: Int = productPrice,
        var discountedPercentage: Float = 0f,
        var isFreeOngkir: Boolean = false,
        var freeOngkirImg: String = ""
) : Visitable<AddToCartVariantAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: AddToCartVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}