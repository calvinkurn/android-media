package com.tokopedia.product.addedit.preview.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.addedit.description.presentation.model.*
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.*
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-04-01.
 */

class GetProductMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(product: Product): ProductInputModel = ProductInputModel(
            mapDetailInputModel(product),
            mapDescriptionInputModel(product),
            mapShipmentInputModel(product),
            mapVariantInputModel(product.variant))

    private fun mapVariantInputModel(variant: Variant): ProductVariantInputModel =
            ProductVariantInputModel(
                    mapProductVariantOption(variant.selections, variant.products),
                    mapProductVariant(variant.products),
                    mapSizeChart(variant.sizecharts).firstOrNull()
            )

    private fun mapSizeChart(sizecharts: List<Picture>): List<ProductPicture> =
            sizecharts.map {
                ProductPicture(
                        it.picID.toLongOrZero(),
                        it.status.toIntOrZero(),
                        "",
                        "",
                        it.urlOriginal,
                        it.urlThumbnail,
                        0,
                        0,
                        it.isFromIG.toIntOrZero()
                )
            }

    private fun mapProductVariant(products: List<ProductVariant>): ArrayList<ProductVariantCombination> {
        val variantCombination = products.map {
            ProductVariantCombination(
                    getActiveStatus(it.status),
                    it.price.toDouble(),
                    it.stock.toLong(),
                    it.sku,
                    mapProductVariantCombination(it.combination)
            )
        }
        return ArrayList(variantCombination)
    }

    private fun mapProductVariantCombination(combination: List<Int>): List<Int> =
            combination.map { it + 1 }

    private fun mapProductVariantOption(
            selections: List<Selection>,
            products: List<ProductVariant>
    ): ArrayList<ProductVariantOptionParent> {
        var position = 0
        val variantOptions = selections.map {
            position++
            ProductVariantOptionParent(
                    it.variantId.toIntOrZero(),
                    it.unitID.toIntOrZero(),
                    position,
                    mapProductVariantOptionChild(it.options, products),
                    it.variantName,
                    it.identifier,
                    it.unitName
            )
        }
        return ArrayList(variantOptions)
    }

    private fun mapProductVariantOptionChild(
            options: List<Option>,
            products: List<ProductVariant>
    ): List<ProductVariantOptionChild>?{
        var pvo = 0
        return options.map {
            pvo += 1 // generate pvo id
            ProductVariantOptionChild(
                    hex = it.hexCode,
                    value = it.value,
                    vuv = correctUnitValueId(it.unitValueID, it.hexCode),
                    pvo = pvo,
                    productPictureViewModelList = mapProductVariantPicture(products, pvo - 1)
            )
        }
    }

    // server still return invalid unitValueID for custom color, so we must correct the ID
    private fun correctUnitValueId(unitValueID: String, hexCode: String): Int {
        return if (unitValueID == INCORRECT_UNIT_VALUE_ID && hexCode == INCORRECT_UNIT_HEXCODE) {
            CORRECTED_UNIT_VALUE_ID
        } else {
            unitValueID.toIntOrZero()
        }
    }

    private fun mapProductVariantPicture(
            productVariant: List<ProductVariant>,
            index: Int
    ): List<ProductPicture> {
        var variantPicture = listOf<ProductPicture>()
        productVariant.forEach { variant ->
            val level1Combination = variant.combination.getOrNull(0)
            level1Combination?.apply {
                if (this == index) {
                    variantPicture = transformProductVariantPicture(variant.pictures.getOrNull(0))
                }
            }
        }
        return variantPicture
    }

    private fun transformProductVariantPicture(picture: Picture?): List<ProductPicture> {
        var variantPicture = listOf<ProductPicture>()
        picture?.let {
            val pictureViewModel = ProductPicture(
                    id = it.picID.toLongOrZero(),
                    fileName = it.fileName,
                    filePath = it.filePath,
                    urlOriginal = it.urlOriginal,
                    urlThumbnail = it.urlThumbnail,
                    x = it.width.toLongOrZero(),
                    y = it.height.toLongOrZero()
            )
            variantPicture = listOf(pictureViewModel)
        }
        return variantPicture
    }

    private fun mapDetailInputModel(product: Product): DetailInputModel =
            DetailInputModel(
                    product.productName,
                    product.category.name,
                    product.category.id,
                    product.catalog.catalogID,
                    product.price,
                    product.stock,
                    product.minOrder,
                    product.condition,
                    product.sku,
                    getActiveStatus(product.status),
                    imageUrlOrPathList = mapImageUrlOrPathList(product),
                    preorder = mapPreorderInputModel(product.preorder),
                    wholesaleList = mapWholeSaleInputModel(product.wholesales),
                    pictureList = mapPictureInputModel(product.pictures)
            )

    private fun mapImageUrlOrPathList(product: Product): MutableList<String> {
        val imageUrlOrPathList = mutableListOf<String>()
        product.pictures.forEach {
            imageUrlOrPathList.add(it.urlOriginal)
        }
        return imageUrlOrPathList
    }

    private fun mapPictureInputModel(pictures: List<Picture>): List<PictureInputModel> =
            pictures.map {
                PictureInputModel(
                        it.picID,
                        it.description,
                        it.filePath,
                        it.fileName,
                        it.width.toIntOrZero(),
                        it.height.toIntOrZero(),
                        it.isFromIG,
                        it.urlOriginal,
                        it.urlThumbnail,
                        it.url300,
                        it.status
                )
            }

    private fun mapPreorderInputModel(preorder: Preorder): PreorderInputModel {
        val timeUnit: Int = when (preorder.timeUnit) {
            UNIT_DAY_STRING -> UNIT_DAY
            UNIT_WEEK_STRING -> UNIT_WEEK
            UNIT_MONTH_STRING -> UNIT_MONTH
            else -> UNIT_DAY
        }
        return PreorderInputModel(
                preorder.duration,
                timeUnit,
                preorder.isActive
        )
    }

    private fun mapWholeSaleInputModel(wholesales: List<Wholesale>): List<WholeSaleInputModel> =
            wholesales.map {
                WholeSaleInputModel(
                        it.price,
                        it.minQty.toString()
                )
            }

    private fun mapDescriptionInputModel(product: Product): DescriptionInputModel =
            DescriptionInputModel(
                    product.description,
                    mapVideoInputModel(product.videos)
            )

    private fun mapVideoInputModel(videos: List<Video>): List<VideoLinkModel> =
            videos.map {
                VideoLinkModel(
                        inputUrl = it.source + getYoutubeDelimiter(it.source) + it.url
                )
            }

    private fun mapShipmentInputModel(product: Product): ShipmentInputModel {
        val weightUnit: Int = when (product.weightUnit) {
            UNIT_GRAM_SRING -> UNIT_GRAM
            UNIT_KILOGRAM_SRING -> UNIT_KILOGRAM
            else -> UNIT_GRAM
        }
        return ShipmentInputModel(
                product.weight,
                weightUnit,
                product.mustInsurance
        )
    }

    companion object {
        const val IS_ACTIVE = 1
        const val IS_INACTIVE = 0
        const val IS_ACTIVE_STRING = "ACTIVE"
        const val IS_INACTIVE_STRING = "INACTIVE"
        const val UNIT_DAY = 0
        const val UNIT_WEEK = 1
        const val UNIT_MONTH = 2
        const val UNIT_GRAM = 0
        const val UNIT_KILOGRAM = 1
        const val UNIT_DAY_STRING = "DAY"
        const val UNIT_WEEK_STRING = "WEEK"
        const val UNIT_MONTH_STRING = "MONTH"
        const val UNIT_GRAM_SRING = "GR"
        const val UNIT_KILOGRAM_SRING = "KG"
        const val INCORRECT_UNIT_VALUE_ID = "1"
        const val CORRECTED_UNIT_VALUE_ID = 0
        const val INCORRECT_UNIT_HEXCODE = "#ffffff"
        const val YOUTUBE_URL_DELIMITER = "/watch?v="
        const val YOUTUBE_URL_DELIMITER_SHORT = "/"
        const val YOUTUBE_URL = "www.youtube.com"

        fun getActiveStatus(type: String) =
                when (type) {
                    IS_INACTIVE_STRING -> IS_INACTIVE
                    IS_ACTIVE_STRING -> IS_ACTIVE
                    else -> IS_INACTIVE
                }

        fun getYoutubeDelimiter(source: String) =
                if (source.contains(YOUTUBE_URL)) YOUTUBE_URL_DELIMITER
                else YOUTUBE_URL_DELIMITER_SHORT
    }
}