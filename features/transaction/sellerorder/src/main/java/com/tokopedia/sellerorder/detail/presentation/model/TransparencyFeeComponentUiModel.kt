package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl

data class TransparencyFeeComponentUiModel(
    val label: String,
    val value: String,
    val attributes: List<BaseTransparencyFeeAttributes>
): BaseTransparencyFee {
    override fun type(typeFactory: DetailTransparencyFeeAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
