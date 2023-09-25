package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl

class TransparencyFeeErrorStateUiModel(
    val throwable: Throwable
): BaseTransparencyFee {
    override fun type(typeFactory: DetailTransparencyFeeAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
