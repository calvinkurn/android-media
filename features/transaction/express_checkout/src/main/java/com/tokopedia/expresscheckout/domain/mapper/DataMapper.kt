package com.tokopedia.expresscheckout.domain.mapper

import com.tokopedia.expresscheckout.data.entity.atc.AtcResponse
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface DataMapper {

    fun convertToDomainModel(atcResponse: AtcResponse): AtcResponseModel

}