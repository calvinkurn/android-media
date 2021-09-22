package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactory

data class BenefitPackageHeaderUiModel(
    val periodDate: String = "",
    val gradeName: String = "",
    val nextUpdate: String = ""
) : BaseBenefitPackageUiModel {
    override fun type(typeFactory: BenefitPackageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}