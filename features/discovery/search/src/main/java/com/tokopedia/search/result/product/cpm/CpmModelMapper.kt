package com.tokopedia.search.result.product.cpm

import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel

object CpmModelMapper {

    fun createCpmDataView(
        cpmModel: CpmModel,
        cpmData: ArrayList<CpmData>,
        isUseBothSeparator: () -> Boolean,
    ): CpmDataView {
        val verticalSeparator =
            if (isUseBothSeparator()) VerticalSeparator.Both
            else VerticalSeparator.None
        val cpmForViewModel = createCpmForViewModel(cpmModel, cpmData)
        return CpmDataView(cpmForViewModel, verticalSeparator)
    }

    private fun createCpmForViewModel(
        cpmModel: CpmModel,
        cpmData: ArrayList<CpmData>,
    ): CpmModel {
        return CpmModel().apply {
            header = cpmModel.header
            status = cpmModel.status
            error = cpmModel.error
            data = cpmData
        }
    }
}