package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoEligibleHeaderUiModel(
        var uiData: UiData
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoEligibleHeaderUiModel

        if (uiData != other.uiData) return false

        return true
    }

    override fun hashCode(): Int {
        return uiData.hashCode()
    }

    data class UiData(
            var title: String = "",
            var subTitle: String = ""
    )

}