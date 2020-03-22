package com.tokopedia.product.addedit.common.domain.mapper

import com.tokopedia.product.addedit.common.domain.model.params.add.*
import com.tokopedia.product.addedit.description.model.DescriptionInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import javax.inject.Inject

/**
 * Created by faisalramd on 2020-03-23.
 */

class AddProductInputMapper @Inject constructor() {
    fun mapInputToRemoteModel(detailInputModel: DetailInputModel,
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
                Catalog(
                        detailInputModel.catalogId
                ),
                Category(
                        detailInputModel.categoryId
                ),
                Menu(
                        "0",
                        ""
                ),
                Pictures(),
                Preorder(
                        1,
                        "1",
                        true
                )

        )
    }
}