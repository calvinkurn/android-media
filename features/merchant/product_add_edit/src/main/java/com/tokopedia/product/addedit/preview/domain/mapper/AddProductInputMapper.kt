package com.tokopedia.product.addedit.preview.domain.mapper

import android.net.Uri
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.description.presentation.model.*
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.model.params.add.*
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class AddProductInputMapper @Inject constructor() {

    companion object {
        const val PRICE_CURRENCY = "IDR"
        const val UNIT_GRAM = "GR"
        const val UNIT_KILOGRAM = "KG"
        const val UNIT_DAY = "DAY"
        const val UNIT_WEEK = "WEEK"
        const val UNIT_MONTH = "MONTH"
        private const val IS_ACTIVE = 1
        private const val IS_INACTIVE = 0
        private const val IS_ACTIVE_STRING = "ACTIVE"
        private const val IS_INACTIVE_STRING = "INACTIVE"

        fun getActiveStatus(status: Int) =
                when (status) {
                    IS_INACTIVE -> IS_INACTIVE_STRING
                    IS_ACTIVE -> IS_ACTIVE_STRING
                    else -> IS_ACTIVE_STRING
                }
    }

    fun mapInputToParam(shopId: String,
                        uploadIdList: ArrayList<String>,
                        detailInputModel: DetailInputModel,
                        descriptionInputModel: DescriptionInputModel,
                        shipmentInputModel: ShipmentInputModel,
                        variantInputModel: VariantInputModel): ProductAddParam {

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
                Catalog(detailInputModel.catalogId),
                Category(detailInputModel.categoryId),
                mapProductShowCases(detailInputModel.productShowCases),
                mapPictureParam(detailInputModel.imageUrlOrPathList, detailInputModel.pictureList, uploadIdList),
                mapPreorderParam(detailInputModel.preorder),
                mapWholesaleParam(detailInputModel.wholesaleList),
                mapVideoParam(descriptionInputModel.videoLinkList),
                mapVariantParam(variantInputModel),
                mapSpecificationParam(detailInputModel.specifications)
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
                mapPictureVariant(it.pictures)
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

    private fun mapSizeChart(sizecharts: PictureVariantInputModel): List<Picture>? {
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

    private fun mapWholesaleParam(wholesaleList: List<WholeSaleInputModel>): Wholesales? {
        val data: ArrayList<Wholesale> = ArrayList()
        wholesaleList.forEach {
            val quantity = it.quantity.replace(".", "").toIntOrZero()
            val price = it.price.replace(".", "")
                    .toBigIntegerOrNull() ?: 0.toBigInteger()
            if (quantity > 1) {
                data.add(Wholesale(
                        quantity,
                        price)
                )
            }
        }
        return Wholesales(data)
    }

    private fun mapShipmentUnit(weightUnit: Int): String? {
        return if (weightUnit == 0) UNIT_GRAM else UNIT_KILOGRAM
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
                    data.add(Picture(
                            description,
                            fileName,
                            filePath,
                            "",
                            isFromIG.contains("true"),
                            width,
                            height
                    ))
                }
            } else {
                val uploadId = uploadIdList.getOrNull(idxUploadIdList++) ?: ""
                data.add(Picture(uploadId = uploadId))
            }
        }
        return Pictures(data)
    }

    private fun mapPreorderParam(preorder: PreorderInputModel): Preorder {
        if (preorder.duration == 0) return Preorder()
        return Preorder(
                preorder.duration,
                when (preorder.timeUnit) {
                    0 -> UNIT_DAY
                    1 -> UNIT_WEEK
                    else -> UNIT_MONTH
                },
                preorder.isActive
        )
    }

    private fun mapSpecificationParam(specifications: List<SpecificationInputModel>?) =
            specifications?.map { it.id }
}