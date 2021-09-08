package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel

interface BenefitPackageAdapterFactory {
    fun type(benefitPackageHeaderUiModel: BenefitPackageHeaderUiModel): Int
    fun type(benefitPackageDataUiModel: BenefitPackageDataUiModel): Int
    fun type(benefitPackageErrorUiModel: BenefitPackageErrorUiModel): Int
}