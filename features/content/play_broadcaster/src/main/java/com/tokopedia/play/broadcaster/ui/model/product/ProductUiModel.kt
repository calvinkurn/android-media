package com.tokopedia.play.broadcaster.ui.model.product

import android.os.Parcelable
import com.tokopedia.play.broadcaster.type.ProductPrice
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
import kotlinx.android.parcel.Parcelize

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
@Parcelize
data class ProductUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val stock: Long,
    val price: ProductPrice,
    val pinStatus: PinProductUiModel = PinProductUiModel.Empty,
) : Parcelable