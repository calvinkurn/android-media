package com.tokopedia.expresscheckout.domain.mapper

import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutResponse
import com.tokopedia.expresscheckout.domain.model.ResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface DataMapper {

    fun convertToDomainModel(expressCheckoutResponse: ExpressCheckoutResponse): ResponseModel

}