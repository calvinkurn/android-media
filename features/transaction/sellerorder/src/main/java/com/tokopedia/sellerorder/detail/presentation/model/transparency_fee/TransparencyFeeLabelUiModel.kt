package com.tokopedia.sellerorder.detail.presentation.model.transparency_fee

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes

data class TransparencyFeeLabelUiModel(
    val label: String,
    val labelType: String
) : BaseTransparencyFeeAttributes {
    override fun type(typeFactory: TransparencyFeeAttributesAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }

}
