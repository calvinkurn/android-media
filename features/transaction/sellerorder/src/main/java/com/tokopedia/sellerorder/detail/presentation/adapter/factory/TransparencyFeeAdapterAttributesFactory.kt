package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSummaryLabelUiModel

interface TransparencyFeeAdapterAttributesFactory {
    fun type(uiModel: TransparencyFeeLabelUiModel): Int
    fun type(uiModel: TransparencyFeeIconUiModel): Int
    fun type(transparencyFeeHeaderLabelUiModel: TransparencyFeeHeaderLabelUiModel): Int
    fun type(transparencyFeeComponentLabelUiModel: TransparencyFeeComponentLabelUiModel): Int
    fun type(transparencyFeeSubComponentLabelUiModel: TransparencyFeeSubComponentLabelUiModel): Int
    fun type(transparencyFeeSummaryLabelUiModel: TransparencyFeeSummaryLabelUiModel): Int
}
