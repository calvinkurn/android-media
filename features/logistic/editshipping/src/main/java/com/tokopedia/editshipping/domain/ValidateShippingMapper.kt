package com.tokopedia.editshipping.domain

import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.response.Data
import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
import rx.functions.Func1

class ValidateShippingMapper : Func1<ValidateShippingResponse, ValidateShippingModel> {
    override fun call(response: ValidateShippingResponse): ValidateShippingModel {
        return ValidateShippingModel().apply {
            this.status = response.response.status
            this.config = response.response.config
            this.serverProcessTime = response.response.serverProcessTime
            this.messageStatus = response.response.messageStatus
            this.data = dataModel(response.response.data)
        }
    }

    private fun dataModel(data: Data): com.tokopedia.editshipping.domain.model.Data {
        return com.tokopedia.editshipping.domain.model.Data().apply {
            this.showPopup = data.showPopup
            this.tickerTitle = data.tickerTitle
            this.tickerContent = data.tickerContent
            this.popupContent = data.popupContent
        }
    }
}
