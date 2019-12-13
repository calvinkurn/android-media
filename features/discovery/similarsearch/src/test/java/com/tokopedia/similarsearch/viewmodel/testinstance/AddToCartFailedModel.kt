package com.tokopedia.similarsearch.viewmodel.testinstance

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel

internal fun getAddToCartFailedModel() = AddToCartDataModel(
        status = AddToCartDataModel.STATUS_ERROR,
        errorMessage = arrayListOf(),
        data = DataModel(
                success = 0
        )
)