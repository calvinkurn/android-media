package com.tokopedia.content.product.picker.sgc.model.product

import android.os.Parcelable
import com.tokopedia.content.product.picker.sgc.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.sgc.model.ProductPrice
import kotlinx.android.parcel.Parcelize

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
@Parcelize
data class ProductUiModel(
    val id: String,
    val name: String,
    val hasCommission: Boolean,
    val commissionFmt: String,
    val commission: Long,
    val extraCommission: Boolean,
    val imageUrl: String,
    val stock: Long,
    val price: ProductPrice,
    val pinStatus: PinProductUiModel,
    val number: String,
) : Parcelable
