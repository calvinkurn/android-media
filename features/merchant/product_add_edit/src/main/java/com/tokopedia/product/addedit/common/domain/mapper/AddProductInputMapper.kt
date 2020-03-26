package com.tokopedia.product.addedit.common.domain.mapper

import com.tokopedia.product.addedit.common.domain.model.params.add.*
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
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
    }

    fun mapInputToParam(shopId:String,
                        uploadIdList: ArrayList<String>,
                        detailInputModel: DetailInputModel,
                        descriptionInputModel: DescriptionInputModel,
                        shipmentInputModel: ShipmentInputModel): ProductAddParam {

        return ProductAddParam(
                detailInputModel.productName,
                detailInputModel.price,
                PRICE_CURRENCY,
                detailInputModel.stock,
                STOCK_STATUS,
                descriptionInputModel.productDescription,
                detailInputModel.minOrder,
                mapShipmentUnit(shipmentInputModel.weightUnit),
                shipmentInputModel.weight,
                detailInputModel.condition,
                shipmentInputModel.isMustInsurance,
                detailInputModel.sku,
                ShopParam(
                        shopId
                ),
                Catalog(
                        detailInputModel.catalogId
                ),
                Category(
                        detailInputModel.categoryId
                ),
                ProductEtalase(), // TODO product etalase not implemented yet
                mapPictureParam(uploadIdList),
                mapPreorderParam(detailInputModel.preorder),
                Wholesales(),
                mapVideoParam(descriptionInputModel.videoLinkList)

        )
    }

    private fun mapShipmentUnit(weightUnit: Int): String? {
        return if (weightUnit == 0) UNIT_GRAM else UNIT_KILOGRAM
    }

    private fun mapVideoParam(videoLinkList: List<VideoLinkModel>): Videos {
        val data: ArrayList<Video> = ArrayList()
        videoLinkList.forEach {
            val urlSplit = it.inputUrl.split("/watch?v=")
            val source = urlSplit[0]
            val url = urlSplit[1]
            data.add(Video(source, url))
        }
        return Videos(data)
    }

    private fun mapPictureParam(uploadIdList: java.util.ArrayList<String>): Pictures {
        val data: ArrayList<PictureId> = ArrayList()
        uploadIdList.forEach {
            data.add(PictureId(it))
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