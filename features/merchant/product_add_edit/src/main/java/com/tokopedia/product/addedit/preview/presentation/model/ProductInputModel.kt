package com.tokopedia.product.addedit.preview.presentation.model

import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel

/**
 * Created by faisalramd on 2020-04-01.
 */

data class ProductInputModel (
        var detailInputModel: DetailInputModel = DetailInputModel(),
        var descriptionInputModel: DescriptionInputModel = DescriptionInputModel(),
        var shipmentInputModel: ShipmentInputModel = ShipmentInputModel(),
        var productId: Long = 0L
)