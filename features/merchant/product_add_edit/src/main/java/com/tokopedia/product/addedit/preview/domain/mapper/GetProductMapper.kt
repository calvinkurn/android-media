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
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-04-01.
 */

class GetProductMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(product: Product): ProductInputModel = ProductInputModel(
            mapDetailInputModel(product),
            mapDescriptionInputModel(product),
            mapShipmentInputModel(product),
            mapVariantInputModel(product.variant),
            itemSold = product.txStats.itemSold
    )

    private fun mapVariantInputModel(variant: Variant): VariantInputModel =
            VariantInputModel(
                    products = mapProductVariants(variant.products),
                    selections = mapProductVariantSelections(variant.selections),
                    sizecharts = mapSizeChart(variant.sizecharts),
                    isRemoteDataHasVariant = variant.selections.isNotEmpty() // if selection is not empty, means server has a variant
            )

    private fun mapSizeChart(sizecharts: List<Picture>): PictureVariantInputModel {
        val sizechart = sizecharts.firstOrNull()
        sizechart?.let {
            return PictureVariantInputModel(
                    it.picID,
                    it.description,
                    it.filePath,
                    it.fileName,
                    it.width.toLongOrZero(),
                    it.height.toLongOrZero(),
                    it.isFromIG,
                    it.urlOriginal,
                    it.urlThumbnail,
                    it.url300,
                    it.status == "true",
                    ""
            )
        }
        return PictureVariantInputModel()
    }

    private fun mapProductVariants(products: List<ProductVariant>): ArrayList<ProductVariantInputModel> {
        val variantCombination = products.map {
            ProductVariantInputModel(
                    it.id,
                    it.combination,
                    mapVariantPictureInputModel(it.pictures),
                    it.price,
                    it.sku,
                    it.status,
                    it.stock,
                    it.isPrimary
            )
        }
        return ArrayList(variantCombination)
    }

    private fun mapVariantPictureInputModel(pictures: List<Picture>): List<PictureVariantInputModel> =
            pictures.map {
                PictureVariantInputModel(
                        it.picID,
                        it.description,
                        it.filePath,
                        it.fileName,
                        it.width.toLongOrZero(),
                        it.height.toLongOrZero(),
                        it.isFromIG,
                        it.urlOriginal,
                        it.urlThumbnail,
                        it.url300,
                        it.status == "0",
                        ""
                )
            }

    private fun mapProductVariantSelections(selections: List<Selection>): List<SelectionInputModel> =
            selections.map {
                SelectionInputModel(
                        it.variantId,
                        it.variantName,
                        it.unitID,
                        it.unitName,
                        it.identifier,
                        mapProductVariantOptions(it.options)
                )
            }

    private fun mapProductVariantOptions(options: List<Option>): List<OptionInputModel> =
            options.map {
                OptionInputModel(
                        it.unitValueID,
                        it.value,
                        it.hexCode
                )
            }

    private fun mapDetailInputModel(product: Product): DetailInputModel =
            DetailInputModel(
                    product.productName,
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
                    pictureList = mapPictureInputModel(product.pictures),
                    productShowCases = mapProductShowCaseInputModel(product.menus),
                    null
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

    private fun mapProductShowCaseInputModel(showCases: List<String>): List<ShowcaseItemPicker> =
            showCases.map {
                ShowcaseItemPicker(showcaseId = it)
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
                        inputUrl = getYoutubeHost(it.source) + getYoutubeDelimiter(it.source) + it.url
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
        const val YOUTUBE_URL_DELIMITER = "/watch?v="
        const val YOUTUBE_URL_DELIMITER_SHORT = "/"
        const val YOUTUBE_URL = "youtube.com"
        const val YOUTUBE_URL_SHORTEN = "youtu.be"
        const val YOUTUBE_SOURCE = "youtube"

        fun getActiveStatus(type: String) =
                when (type) {
                    IS_INACTIVE_STRING -> IS_INACTIVE
                    IS_ACTIVE_STRING -> IS_ACTIVE
                    else -> IS_INACTIVE
                }

        fun getYoutubeDelimiter(source: String) =
                if (source.contains(YOUTUBE_URL)) YOUTUBE_URL_DELIMITER
                else YOUTUBE_URL_DELIMITER_SHORT

        fun getYoutubeHost(source: String) =
                if (source == YOUTUBE_SOURCE) YOUTUBE_URL_SHORTEN
                else source
    }
}