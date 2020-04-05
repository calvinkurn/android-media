package com.tokopedia.product.addedit.preview.domain.mapper

import android.util.Log
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
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
            mapShipmentInputModel(product)
    )

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
                    imageUrlOrPathList = mapImageUrlOrPathList(product),
                    preorder = mapPreorderInputModel(product.preorder),
                    wholesaleList = mapWholeSaleInputModel(product.wholesales),
                    pictureList = mapPictureInputModel(product.pictures)
            )

    private fun mapImageUrlOrPathList(product: Product): MutableList<String> {
        val imageUrlOrPathList = mutableListOf<String>()
        product.pictures.forEach {
            imageUrlOrPathList.add(it.filePath)
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
            else -> -1
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
                        inputImage = it.source + YOUTUBE_URL_DELIMITER + it.url
                )
            }

    private fun mapShipmentInputModel(product: Product): ShipmentInputModel {
        val weightUnit: Int = when (product.weightUnit) {
            UNIT_GRAM_SRING -> UNIT_GRAM
            UNIT_KILOGRAM_SRING -> UNIT_KILOGRAM
            else -> -1
        }
        return ShipmentInputModel(
                product.weight,
                weightUnit,
                product.mustInsurance
        )
    }

    companion object {
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
        const val YOUTUBE_URL_DELIMITER = "watch?v="
    }
}