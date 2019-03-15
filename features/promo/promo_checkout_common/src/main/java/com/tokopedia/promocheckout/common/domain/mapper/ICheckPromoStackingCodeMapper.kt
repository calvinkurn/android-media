package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Data
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel

/**
 * Created by fwidjaja on 14/03/19.
 */
interface ICheckPromoStackingCodeMapper {
    fun convertResponseDataModel(response: Data): DataUiModel?
}