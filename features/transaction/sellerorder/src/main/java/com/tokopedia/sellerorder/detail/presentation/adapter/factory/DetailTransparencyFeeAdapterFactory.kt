package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeLoadingUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentUiModel

interface DetailTransparencyFeeAdapterFactory {
    fun type(uiModel: TransparencyFeeHeaderUiModel): Int
    fun type(uiModel: TransparencyFeeComponentUiModel): Int
    fun type(uiModel: TransparencyFeeSubComponentUiModel): Int
    fun type(uiModel: TransparencyFeeLoadingUiModel): Int
    fun type(uiModel: TransparencyFeeErrorStateUiModel): Int
}
