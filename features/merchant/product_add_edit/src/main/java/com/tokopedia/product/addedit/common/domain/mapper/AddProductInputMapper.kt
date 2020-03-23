package com.tokopedia.product.addedit.common.domain.mapper

import com.tokopedia.product.addedit.common.domain.model.params.add.*
import com.tokopedia.product.addedit.description.model.DescriptionInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class AddProductInputMapper @Inject constructor() {
    fun mapInputToParam(shopId:String,
                        detailInputModel: DetailInputModel,
                        descriptionInputModel: DescriptionInputModel,
                        shipmentInputModel: ShipmentInputModel): ProductAddParam {

        return ProductAddParam(
                detailInputModel.productName,
                detailInputModel.price,
                "IDR",
                detailInputModel.stock,
                "LIMITED",
                descriptionInputModel.productDescription,
                detailInputModel.minOrder,
                "GR",
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
                ProductEtalase(
                        "0",
                        ""
                ),
                Pictures(),
                mapPreorderParam(detailInputModel.preorder)

        )
    }

    private fun mapPreorderParam(preorder: PreorderInputModel): Preorder {
        if (preorder.duration == 0) return Preorder()
        return Preorder(
                preorder.duration,
                when (preorder.timeUnit) {
                    0 -> "DAY"
                    1 -> "WEEK"
                    else -> "Month"
                },
                preorder.isActive
        )
    }
}