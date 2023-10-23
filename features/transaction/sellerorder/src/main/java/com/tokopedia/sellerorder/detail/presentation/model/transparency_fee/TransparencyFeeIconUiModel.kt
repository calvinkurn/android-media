package com.tokopedia.sellerorder.detail.presentation.model.transparency_fee

import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl

class TransparencyFeeIconUiModel(
    val iconUrl: String,
    val darkIconUrl: String,
    val transparencyFeeInfo: TransparencyFeeInfo
) : BaseTransparencyFeeAttributes {
    override fun type(typeFactory: TransparencyFeeAttributesAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }

    data class TransparencyFeeInfo(
        val title: String,
        val desc: String
    ) {
        fun hasTooltip(): Boolean {
            return desc.isNotBlank()
        }
    }
}
