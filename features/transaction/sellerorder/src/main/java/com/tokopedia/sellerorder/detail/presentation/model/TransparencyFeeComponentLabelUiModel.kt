package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl

data class TransparencyFeeComponentLabelUiModel(
    val text: String
) : BaseTransparencyFeeAttributes {
    override fun type(typeFactory: TransparencyFeeAttributesAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
