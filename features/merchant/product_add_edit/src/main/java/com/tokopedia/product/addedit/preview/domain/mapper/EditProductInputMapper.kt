package com.tokopedia.product.addedit.preview.domain.mapper

import android.net.Uri
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.model.params.add.*
import com.tokopedia.product.addedit.preview.data.model.params.edit.ProductEditParam
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.IS_ACTIVE
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.PRICE_CURRENCY
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_GRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.UNIT_KILOGRAM_STRING
import com.tokopedia.product.addedit.preview.domain.constant.ProductMapperConstants.getActiveStatus
import com.tokopedia.product.addedit.shipment.presentation.model.CPLModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class EditProductInputMapper @Inject constructor() {

    fun mapInputToParam(shopId: String,
                        productId: String,
                        uploadIdList: ArrayList<String>,
                        detailInputModel: DetailInputModel,
                        descriptionInputModel: DescriptionInputModel,
                        shipmentInputModel: ShipmentInputModel,
                        variantInputModel: VariantInputModel,
                        shouldPutStockOnParam: Boolean = true): ProductEditParam {

        // Put null to stock and status param as we will update product stock separately (related to multilocation)
        val stock: Int? = if (shouldPutStockOnParam) detailInputModel.stock else null
        val status: Int? = if (detailInputModel.status != IS_ACTIVE || shouldPutStockOnParam || detailInputModel.stock > 0) detailInputModel.status else null

        return ProductEditParam(
                productId,
                detailInputModel.productName,
                detailInputModel.price,
                PRICE_CURRENCY,
                stock,
                status?.let { getActiveStatus(it) },
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
            if (variantInputModel.isRemoteDataHasVariant) {
                Variant()
            } else {
                null
            }
        } else {
            Variant(
                    mapVariantSelections(variantInputModel.selections),
                    mapVariantProducts(variantInputModel.products),
                    mapSizeChart(variantInputModel.sizecharts)
            )
        }
    }

    private fun mapCPLData(cpl: CPLModel): CPLData? {
        return cpl.shipmentServicesIds?.let {  CPLData(it) }
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
        val filePath = it.pictures.firstOrNull()?.filePath ?: ""
        val picID = it.pictures.firstOrNull()?.picID ?: ""
        var productPicture = it.pictures

        if (filePath.startsWith(AddEditProductConstants.HTTP_PREFIX)) {
            productPicture = getExistingPictureFromProductVariants(filePath, picID, products)
        }
        Product(
                it.combination,
                it.price,
                it.sku,
                it.status,
                it.stock,
                it.isPrimary,
                mapPictureVariant(productPicture),
                it.weight.orZero(),
                it.weightUnit
        )
    }

    private fun getExistingPictureFromProductVariants(
            filePath: String,
            picID: String,
            products: List<ProductVariantInputModel>
    ): List<PictureVariantInputModel> {
        val existingPicture = products.find {
            it.pictures.firstOrNull()?.filePath == filePath && picID.isNotEmpty()
        }?.pictures

        return existingPicture ?: emptyList()
    }

    private fun mapPictureVariant(pictures: List<PictureVariantInputModel>) = pictures.map {
        Picture(
                it.description,
                it.fileName,
                it.filePath,
                it.picID,
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
                    sizecharts.picID,
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
                data.add(Wholesale(
                        quantity,
                        price)
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

    private fun mapPictureParam(imageUrlOrPathList: List<String>,
                                pictureList: List<PictureInputModel>,
                                uploadIdList: List<String>): Pictures {
        val data: ArrayList<Picture> = ArrayList()
        var idxPictureList = 0
        var idxUploadIdList = 0
        imageUrlOrPathList.forEach { urlOrPath ->
            if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) {
                with (pictureList[idxPictureList++]) {
                    data.add(Picture(
                            description,
                            fileName,
                            filePath,
                            picID,
                            isFromIG.contains("true")
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
        if (preorder.duration == Int.ZERO) return Preorder()
        return Preorder(
            preorder.duration,
            ProductMapperConstants.getTimeUnitString(preorder.timeUnit),
            preorder.isActive
        )
    }

    private fun mapSpecificationParam(specifications: List<SpecificationInputModel>?): List<String>? =
            specifications?.map { it.id }
}