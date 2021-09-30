package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactory
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel

data class BenefitPackageErrorUiModel(val throwable: Throwable) : BaseBenefitPackageUiModel {
    override fun type(typeFactory: BenefitPackageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}