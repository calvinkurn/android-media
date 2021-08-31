package com.tokopedia.attachproduct.view.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachproduct.view.adapter.NewAttachProductListAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by Hendri on 13/02/18.
 */
@Parcelize
data class NewAttachProductItemUiModel(
    val productUrl: String,
    val productName: String,
    val productId: String,
    val productImageFull: String,
    val productImage: String,
    val productPrice: String,
    val shopName: String,
    val originalPrice: String,
    val discountPercentage: String,
    val isFreeOngkirActive: Boolean,
    val imgUrlFreeOngkir: String,
    val stock: Int
)
    : Visitable<NewAttachProductListAdapterTypeFactory>, Parcelable {

    override fun type(typeFactoryNew: NewAttachProductListAdapterTypeFactory): Int {
        return typeFactoryNew.type(this)
    }
}
