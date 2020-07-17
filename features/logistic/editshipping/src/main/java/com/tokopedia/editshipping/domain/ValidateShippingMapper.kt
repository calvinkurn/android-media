package com.tokopedia.editshipping.domain

import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
import rx.functions.Func1

class ValidateShippingMapper : Func1<ValidateShippingResponse, ValidateShippingModel> {
    override fun call(t: ValidateShippingResponse?): ValidateShippingModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
