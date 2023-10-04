package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryLabelUiModel

interface TransparencyFeeAdapterAttributesFactory {
    fun type(uiModel: TransparencyFeeLabelUiModel): Int
    fun type(uiModel: TransparencyFeeIconUiModel): Int
    fun type(transparencyFeeHeaderLabelUiModel: TransparencyFeeHeaderLabelUiModel): Int
    fun type(transparencyFeeComponentLabelUiModel: TransparencyFeeComponentLabelUiModel): Int
    fun type(transparencyFeeComponentSubLabelUiModel: TransparencyFeeSubComponentLabelUiModel): Int
    fun type(transparencyFeeSummaryLabelUiModel: TransparencyFeeSummaryLabelUiModel): Int
}
