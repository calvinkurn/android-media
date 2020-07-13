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
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class AddProductInputMapper @Inject constructor() {

    companion object{
        const val PRICE_CURRENCY = "IDR"
        const val STOCK_STATUS = "LIMITED"
        const val UNIT_GRAM = "GR"
        const val UNIT_KILOGRAM = "KG"
        const val UNIT_DAY = "DAY"
        const val UNIT_WEEK = "WEEK"
        const val UNIT_MONTH = "MONTH"
        const val IS_ACTIVE = 1
        const val IS_INACTIVE = 0
        const val IS_ACTIVE_STRING = "ACTIVE"
        const val IS_INACTIVE_STRING = "INACTIVE"

        fun getActiveStatus(status: Int) =
                when (status) {
                    IS_INACTIVE -> IS_INACTIVE_STRING
                    IS_ACTIVE -> IS_ACTIVE_STRING
                    else -> IS_ACTIVE_STRING
                }
    }

    fun mapInputToParam(shopId: String,
                        uploadIdList: ArrayList<String>,
                        variantOptionUploadId: List<String>,
                        sizeChartUploadId: String,
                        detailInputModel: DetailInputModel,
                        descriptionInputModel: DescriptionInputModel,
                        shipmentInputModel: ShipmentInputModel,
                        variantInputModel: ProductVariantInputModel): ProductAddParam {

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
                ProductEtalase(),
                mapPictureParam(detailInputModel.imageUrlOrPathList, detailInputModel.pictureList, uploadIdList),
                mapPreorderParam(detailInputModel.preorder),
                mapWholesaleParam(detailInputModel.wholesaleList),
                mapVideoParam(descriptionInputModel.videoLinkList),
                mapVariantParam(variantInputModel, sizeChartUploadId, variantOptionUploadId)

        )
    }

    private fun mapVariantParam(variantInputModel: ProductVariantInputModel,
                                sizeChartUploadId: String,
                                variantOptionUploadId: List<String>): Variant? {
        if (variantInputModel.productVariant.size == 0) {
            return null
        }

        return Variant(
                mapVariantSelectionParam(variantInputModel.variantOptionParent),
                mapVariantProducts(variantInputModel.productVariant, variantOptionUploadId),
                mapVariantSizeChart(variantInputModel.productSizeChart, sizeChartUploadId)
        )
    }

    private fun mapVariantSizeChart(productSizeChart: PictureViewModel?,
                                    sizeChartUploadId: String): List<Picture> {
        val sizeCharts: ArrayList<Picture> = ArrayList()
        productSizeChart?.let {
            if (productSizeChart.filePath.isNotEmpty()) {
                val sizeChart = Picture(uploadId = sizeChartUploadId)
                sizeCharts.add(sizeChart)
            }
        }
        return sizeCharts
    }

    private fun mapVariantProducts(
            productVariant: ArrayList<ProductVariantCombinationViewModel>,
            variantOptionUploadId: List<String>): List<Product> {
        val products: ArrayList<Product> = ArrayList()
        productVariant.forEach {
            val levelIndex = it.opt.firstOrNull()
            val product = Product(
                    mapProductCombination(it.opt),
                    it.priceVar.toBigDecimal().toBigInteger(),
                    it.sku,
                    getActiveStatus(it.st),
                    it.stock,
                    getVariantImage(variantOptionUploadId, levelIndex)
            )
            products.add(product)
        }
        return products
    }

    private fun getVariantImage(variantOptionUploadId: List<String>, index: Int?): List<Picture> {
        var variantPictureList = listOf<Picture>()
        index?.apply {
            variantOptionUploadId.getOrNull(this - 1)?.apply {
                variantPictureList = listOf(Picture(uploadId = this))
            }
        }
        return variantPictureList
    }

    private fun mapProductCombination(opt: List<Int>): List<Int> = opt.map { it - 1 }

    private fun mapVariantSelectionParam(
            variantOptionParent: List<ProductVariantOptionParent>): List<Selection> {
        val selections: ArrayList<Selection> = ArrayList()
        variantOptionParent.forEach {
            val selection = Selection(
                    it.v.toString(),
                    it.vu.toString(),
                    mapVariantOptionParam(it.productVariantOptionChild ?: emptyList())
            )
            selections.add(selection)
        }
        return selections
    }

    private fun mapVariantOptionParam(
            productVariantOptionChild: List<ProductVariantOptionChild>): List<Option> {
        val options: ArrayList<Option> = ArrayList()
        productVariantOptionChild.forEach {
            val option = Option(
                    it.value,
                    it.vuv.toString(),
                    it.hex

            )
            options.add(option)
        }
        return options
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
                with (pictureList[idxPictureList++]) {
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
                data.add(Picture(uploadId = uploadIdList[idxUploadIdList++]))
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
}