package com.tokopedia.expresscheckout.domain.mapper

import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.transactiondata.entity.response.expresscheckoutform.ExpressCheckoutFormDataResponse

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToExpressCheckoutFormData(expressCheckoutFormDataResponse: ExpressCheckoutFormDataResponse): ExpressCheckoutFormData

}