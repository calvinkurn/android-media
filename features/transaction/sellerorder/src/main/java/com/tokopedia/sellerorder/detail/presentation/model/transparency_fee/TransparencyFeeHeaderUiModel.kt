package com.tokopedia.sellerorder.detail.presentation.model.transparency_fee

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFee
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes

data class TransparencyFeeHeaderUiModel(
    val value: String,
    val attributes: List<BaseTransparencyFeeAttributes>,
    val isFirstHeader: Boolean,
    val hasComponents: Boolean
) : BaseTransparencyFee {
    override fun type(typeFactory: DetailTransparencyFeeAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
