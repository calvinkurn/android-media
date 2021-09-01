package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactory


data class BenefitPackageGradeUiModel(
    val benefitPackageData: List<BenefitPackageDataUiModel> = emptyList()
)

data class BenefitPackageDataUiModel(
    val gradeName: String = "",
    val iconBenefitUrl: String = "",
    val descBenefit: String = "",
    val benefitItemList: List<BenefitItem> = emptyList()
) : BaseBenefitPackageUiModel {
    override fun type(typeFactory: BenefitPackageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class BenefitItem(
    val imageUrL: String = "",
    val title: String = ""
)