package com.tokopedia.activation.domain.mapper

import com.tokopedia.activation.model.ShippingEditorModel
import com.tokopedia.activation.model.response.ActivatedShipping

object ShippingEditorMapper {

    fun convertToUIModel(data: ActivatedShipping): ShippingEditorModel {
        return  ShippingEditorModel().apply {
            x11 = data.x11.isAvailable
        }
    }

}