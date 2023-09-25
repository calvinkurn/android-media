package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl

data class TransparencyFeeLabelUiModel(
    val label: String,
    val labelType: String
) : BaseTransparencyFeeAttributes {
    override fun type(typeFactory: TransparencyFeeAttributesAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }

}
