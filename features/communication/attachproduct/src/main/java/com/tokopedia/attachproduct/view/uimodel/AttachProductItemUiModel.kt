package com.tokopedia.attachproduct.view.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapterTypeFactory
import kotlinx.parcelize.Parcelize

/**
 * Created by Hendri on 13/02/18.
 */
@Parcelize
data class AttachProductItemUiModel(
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
    val stock: Int,
    val isSupportVariant: Boolean,
    val isPreorder: Boolean,
    val priceInt: Long,
    val categoryId: Long
) : Visitable<AttachProductListAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: AttachProductListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun toResultProduct(): ResultProduct = ResultProduct(
            this.productId,
            this.productUrl,
            this.productImage,
            this.productPrice,
            this.productName,
            this.originalPrice,
            this.discountPercentage,
            this.isFreeOngkirActive,
            this.imgUrlFreeOngkir,
            this.stock,
            this.isSupportVariant,
            isPreorder = this.isPreorder,
            priceInt = this.priceInt,
            categoryId = this.categoryId
    )
}
