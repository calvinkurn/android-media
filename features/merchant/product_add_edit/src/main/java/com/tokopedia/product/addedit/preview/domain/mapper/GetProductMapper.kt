package com.tokopedia.product.addedit.preview.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Option
import com.tokopedia.product.addedit.preview.data.source.api.response.Picture
import com.tokopedia.product.addedit.preview.data.source.api.response.Preorder
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.data.source.api.response.ProductVariant
import com.tokopedia.product.addedit.preview.data.source.api.response.Selection
import com.tokopedia.product.addedit.preview.data.source.api.response.Variant
import com.tokopedia.product.addedit.preview.data.source.api.response.Video
import com.tokopedia.product.addedit.preview.data.source.api.response.Wholesale
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_DAY
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_DAY_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM_TO_KILOGRAM_MULTIPLIER
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_KILOGRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_MONTH
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_MONTH_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_WEEK
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_WEEK_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getActiveStatus
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getYoutubeDelimiter
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getYoutubeHost
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.CPLModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
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
        itemSold = product.txStats.itemSold,
        hasDTStock = product.hasDTStock,
        isCampaignActive = product.campaign.isActive
    )

    fun convertToGram(weight: Int, unit: String): Int {
        return if (unit == UNIT_KILOGRAM_STRING) weight * UNIT_GRAM_TO_KILOGRAM_MULTIPLIER else weight
    }

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
                it.isPrimary,
                convertToGram(it.weight, it.weightUnit),
                UNIT_GRAM_STRING,
                it.hasDTStock,
                it.isCampaign
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
        return ShipmentInputModel(
            convertToGram(product.weight, product.weightUnit),
            UNIT_GRAM,
            product.mustInsurance,
            CPLModel(
                cplParam = product.cpl.shipperServices
            ),
            product.variant.products.isEmpty()
        )
    }
}
