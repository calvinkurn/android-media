package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl

data class TransparencyFeeSubComponentUiModel(
    val isFirstIndex: Boolean,
    val isLastIndex: Boolean,
    val label: String,
    val value: String,
    val attributes: List<BaseTransparencyFeeAttributes>
) : BaseTransparencyFee {
    override fun type(typeFactory: DetailTransparencyFeeAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
