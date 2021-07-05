package com.tokopedia.product.addedit.variant.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class VariantInputModel(
        var products: List<ProductVariantInputModel> = listOf(),
        var selections: List<SelectionInputModel> = listOf(),
        var sizecharts: PictureVariantInputModel = PictureVariantInputModel(),
        var isRemoteDataHasVariant: Boolean = false // used for removing variant
) : Parcelable

@Parcelize
data class ProductVariantInputModel(
        var id: String = "",
        var combination: List<Int> = listOf(),
        var pictures: List<PictureVariantInputModel> = listOf(),
        var price: BigInteger = 0.toBigInteger(),
        var sku: String = "",
        var status: String = "",
        var stock: Int = 0,
        var isPrimary: Boolean = false
) : Parcelable

@Parcelize
data class SelectionInputModel(
        var variantId: String = "",
        var variantName: String = "",
        var unitID: String = "",
        var unitName: String = "",
        var identifier: String = "",
        var options: List<OptionInputModel> = listOf()
) : Parcelable

@Parcelize
data class OptionInputModel(
        var unitValueID: String = "",
        var value: String = "",
        var hexCode: String = ""
) : Parcelable

@Parcelize
data class PictureVariantInputModel(
        var picID: String = "",
        var description: String = "",
        var filePath: String = "",
        var fileName: String = "",
        var width: Long = 0,
        var height: Long = 0,
        var isFromIG: String = "",
        var urlOriginal: String = "",
        var urlThumbnail: String = "",
        var url300: String = "",
        var status: Boolean = false,
        var uploadId: String = ""
) : Parcelable