package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel

interface TransparencyFeeAdapterAttributesFactory {
    fun type(uiModel: TransparencyFeeLabelUiModel): Int
    fun type(uiModel: TransparencyFeeIconUiModel): Int
}
