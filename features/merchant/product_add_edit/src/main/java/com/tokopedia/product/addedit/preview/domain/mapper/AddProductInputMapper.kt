package com.tokopedia.product.addedit.preview.domain.mapper

import android.net.Uri
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.CategoryMetadataInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.model.params.add.CPLData
import com.tokopedia.product.addedit.preview.data.model.params.add.Category
import com.tokopedia.product.addedit.preview.data.model.params.add.Metadata
import com.tokopedia.product.addedit.preview.data.model.params.add.Option
import com.tokopedia.product.addedit.preview.data.model.params.add.Picture
import com.tokopedia.product.addedit.preview.data.model.params.add.Pictures
import com.tokopedia.product.addedit.preview.data.model.params.add.Preorder
import com.tokopedia.product.addedit.preview.data.model.params.add.Product
import com.tokopedia.product.addedit.preview.data.model.params.add.ProductAddParam
import com.tokopedia.product.addedit.preview.data.model.params.add.ProductEtalase
import com.tokopedia.product.addedit.preview.data.model.params.add.Selection
import com.tokopedia.product.addedit.preview.data.model.params.add.ShopParam
import com.tokopedia.product.addedit.preview.data.model.params.add.Variant
import com.tokopedia.product.addedit.preview.data.model.params.add.Video
import com.tokopedia.product.addedit.preview.data.model.params.add.Videos
import com.tokopedia.product.addedit.preview.data.model.params.add.Wholesale
import com.tokopedia.product.addedit.preview.data.model.params.add.Wholesales
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.PRICE_CURRENCY
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_KILOGRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getActiveStatus
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getTimeUnitString
import com.tokopedia.product.addedit.shipment.presentation.model.CPLModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class AddProductInputMapper @Inject constructor() {

    fun mapInputToParam(
        shopId: String,
        uploadIdList: ArrayList<String>,
        detailInputModel: DetailInputModel,
        descriptionInputModel: DescriptionInputModel,
        shipmentInputModel: ShipmentInputModel,
        variantInputModel: VariantInputModel
    ): ProductAddParam {
        return ProductAddParam(
            detailInputModel.productName,
            detailInputModel.price,
            PRICE_CURRENCY,
            detailInputModel.stock,
            getActiveStatus(detailInputModel.status),
            descriptionInputModel.productDescription,
            detailInputModel.minOrder,
            mapShipmentUnit(shipmentInputModel.weightUnit),
            shipmentInputModel.weight,
            detailInputModel.condition,
            shipmentInputModel.isMustInsurance,
            detailInputModel.sku,
            ShopParam(shopId),
            Category(detailInputModel.categoryId),
            mapProductShowCases(detailInputModel.productShowCases),
            mapPictureParam(detailInputModel.imageUrlOrPathList, detailInputModel.pictureList, uploadIdList),
            mapPreorderParam(detailInputModel.preorder),
            mapWholesaleParam(detailInputModel.wholesaleList),
            mapVideoParam(descriptionInputModel.videoLinkList),
            mapVariantParam(variantInputModel),
            mapCPLData(shipmentInputModel.cplModel),
            mapSpecificationParam(detailInputModel.specifications),
            mapMetadataParam(detailInputModel.categoryMetadata)
        )
    }

    private fun mapProductShowCases(productShowCases: List<ShowcaseItemPicker>): List<ProductEtalase> =
        productShowCases.map {
            ProductEtalase(menuID = it.showcaseId, name = it.showcaseName)
        }

    private fun mapVariantParam(variantInputModel: VariantInputModel): Variant? {
        return if (variantInputModel.selections.isEmpty()) {
            // if there is no variant input then return null
            null
        } else {
            Variant(
                mapVariantSelections(variantInputModel.selections),
                mapVariantProducts(variantInputModel.products),
                mapSizeChart(variantInputModel.sizecharts)
            )
        }
    }

    private fun mapVariantSelections(selections: List<SelectionInputModel>) = selections.map {
        Selection(
            it.variantId,
            it.unitID,
            it.variantName,
            mapVariantOptions(it.options)
        )
    }

    private fun mapVariantOptions(options: List<OptionInputModel>) = options.map {
        Option(
            it.value,
            it.unitValueID,
            it.hexCode
        )
    }

    private fun mapVariantProducts(products: List<ProductVariantInputModel>) = products.map {
        Product(
            it.combination,
            it.price,
            it.sku,
            it.status,
            it.stock,
            it.isPrimary,
            mapPictureVariant(it.pictures),
            it.weight.orZero(),
            it.weightUnit
        )
    }

    private fun mapPictureVariant(pictures: List<PictureVariantInputModel>) = pictures.map {
        Picture(
            it.description,
            it.fileName,
            it.filePath,
            "",
            it.isFromIG == "true",
            it.width.toInt(),
            it.height.toInt(),
            it.uploadId
        )
    }

    private fun mapSizeChart(sizecharts: PictureVariantInputModel): List<Picture> {
        return if (sizecharts.urlOriginal.isEmpty() && sizecharts.uploadId.isEmpty()) {
            emptyList()
        } else {
            val sizechart = Picture(
                sizecharts.description,
                sizecharts.fileName,
                sizecharts.filePath,
                "",
                sizecharts.isFromIG == "true",
                sizecharts.width.toInt(),
                sizecharts.height.toInt(),
                sizecharts.uploadId
            )

            listOf(sizechart)
        }
    }

    private fun mapWholesaleParam(wholesaleList: List<WholeSaleInputModel>): Wholesales {
        val data: ArrayList<Wholesale> = ArrayList()
        wholesaleList.forEach {
            val quantity = it.quantity.replace(".", "").toIntOrZero()
            val price = it.price.replace(".", "")
                .toBigIntegerOrNull() ?: 0.toBigInteger()
            if (quantity > 1) {
                data.add(
                    Wholesale(
                        quantity,
                        price
                    )
                )
            }
        }
        return Wholesales(data)
    }

    private fun mapShipmentUnit(weightUnit: Int): String {
        return if (weightUnit == UNIT_GRAM) UNIT_GRAM_STRING else UNIT_KILOGRAM_STRING
    }

    private fun mapVideoParam(videoLinkList: List<VideoLinkModel>): Videos {
        val data: ArrayList<Video> = ArrayList()
        videoLinkList.forEach {
            if (it.inputUrl.isNotEmpty()) {
                val uri = Uri.parse(it.inputUrl)
                val source = uri.host ?: uri.pathSegments[0] ?: ""
                val url = uri.getQueryParameter("v") ?: uri.lastPathSegment
                data.add(Video(source, url))
            }
        }
        return Videos(data)
    }

    private fun mapPictureParam(imageUrlOrPathList: List<String>, pictureList: List<PictureInputModel>, uploadIdList: ArrayList<String>): Pictures {
        val data: ArrayList<Picture> = ArrayList()
        var idxPictureList = 0
        var idxUploadIdList = 0
        imageUrlOrPathList.forEach { urlOrPath ->
            if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) {
                with(pictureList[idxPictureList++]) {
                    data.add(
                        Picture(
                            description,
                            fileName,
                            filePath,
                            "",
                            isFromIG.contains("true"),
                            width,
                            height
                        )
                    )
                }
            } else {
                val uploadId = uploadIdList.getOrNull(idxUploadIdList++) ?: ""
                data.add(Picture(uploadId = uploadId))
            }
        }
        return Pictures(data)
    }

    private fun mapPreorderParam(preorder: PreorderInputModel): Preorder {
        if (preorder.duration == Int.ZERO) return Preorder()
        return Preorder(
            preorder.duration,
            getTimeUnitString(preorder.timeUnit),
            preorder.isActive
        )
    }

    private fun mapCPLData(cpl: CPLModel): CPLData? {
        return cpl.shipmentServicesIds?.let { CPLData(it) }
    }

    private fun mapSpecificationParam(specifications: List<SpecificationInputModel>?) =
        specifications?.map { it.id }

    private fun mapMetadataParam(categoryMetadataInputModel: CategoryMetadataInputModel): Metadata {
        return Metadata(
            Metadata.Category(
                isFromRecommendation = categoryMetadataInputModel.isFromRecommendation,
                recommendationRank = categoryMetadataInputModel.recommendationRank,
                recommendationList = categoryMetadataInputModel.recommendationList.map {
                    Metadata.Recommendation(
                        categoryId = it.categoryID,
                        confidenceScore = it.confidenceScore,
                        precision = it.precision
                    )
                }
            )
        )
    }
}
