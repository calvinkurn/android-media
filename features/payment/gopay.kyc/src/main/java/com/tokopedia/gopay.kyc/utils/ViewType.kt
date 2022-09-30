package com.tokopedia.gopay.kyc.utils

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit

interface ViewType {
    fun type(dataModel: GoPayPlusBenefit): Int
    fun type(dataModel: EmptyModel):Int
}