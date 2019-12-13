package com.tokopedia.similarsearch.viewmodel.testinstance

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel

internal fun getAddToCartSuccessModel() = AddToCartDataModel(
        status = AddToCartDataModel.STATUS_OK,
        data = DataModel(
                success = 1,
                cartId = 12345,
                message = arrayListOf()
        )
)