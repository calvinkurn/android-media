package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLoadingUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentUiModel

interface DetailTransparencyFeeAdapterFactory {
    fun type(uiModel: TransparencyFeeHeaderUiModel): Int
    fun type(uiModel: TransparencyFeeComponentUiModel): Int
    fun type(uiModel: TransparencyFeeSubComponentUiModel): Int
    fun type(uiModel: TransparencyFeeLoadingUiModel): Int
    fun type(uiModel: TransparencyFeeErrorStateUiModel): Int
}
