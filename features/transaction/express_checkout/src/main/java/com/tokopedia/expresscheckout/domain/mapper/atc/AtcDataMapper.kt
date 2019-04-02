package com.tokopedia.expresscheckout.domain.mapper.atc

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcResponse
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface AtcDataMapper {

    fun convertToDomainModel(atcResponse: AtcResponse): AtcResponseModel

}