package com.tokopedia.product.addedit.variant.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM_STRING
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

enum class VariantStockStatus{
        ALL_EMPTY,
        ALL_AVAILABLE,
        PARTIALLY_AVAILABLE
}

@Parcelize
data class VariantInputModel(
        var products: List<ProductVariantInputModel> = listOf(),
        var selections: List<SelectionInputModel> = listOf(),
        var sizecharts: PictureVariantInputModel = PictureVariantInputModel(),
        var isRemoteDataHasVariant: Boolean = false // used for removing variant
) : Parcelable {
        fun hasVariant() = products.isNotEmpty()

        fun getPrimaryVariantData(
                defaultIfNotfound: ProductVariantInputModel = ProductVariantInputModel()
        ) = products.find { it.isPrimary } ?: defaultIfNotfound

        fun getLowestPrice() = products.minByOrNull { it.price }?.price

        fun getHighestPrice() = products.maxByOrNull { it.price }?.price

        fun getTotalStock(): Int = products.sumOf { it.stock.orZero() }

        fun getStockStatus(): VariantStockStatus {
                return when {
                    products.all { it.stock.orZero().isMoreThanZero() } -> VariantStockStatus.ALL_AVAILABLE
                    products.all { it.stock.orZero().isZero() } -> VariantStockStatus.ALL_EMPTY
                    else -> VariantStockStatus.PARTIALLY_AVAILABLE
                }
        }

        fun getImageUrl(colorVariantLevel: Int, index: Int) = products.find {
                it.combination.getOrNull(colorVariantLevel) == index
        }?.pictures?.firstOrNull()

        fun isVariantCampaignActive() = products.any { it.isCampaign }
}

@Parcelize
data class ProductVariantInputModel(
        var id: String = "",
        var combination: List<Int> = listOf(),
        var pictures: List<PictureVariantInputModel> = listOf(),
        var price: BigInteger = 0.toBigInteger(),
        var sku: String = "",
        var status: String = "",
        var stock: Int? = null,
        var isPrimary: Boolean = false,
        var weight: Int? = null,
        var weightUnit: String = UNIT_GRAM_STRING,
        var hasDTStock: Boolean = false,
        var isCampaign: Boolean = false
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
