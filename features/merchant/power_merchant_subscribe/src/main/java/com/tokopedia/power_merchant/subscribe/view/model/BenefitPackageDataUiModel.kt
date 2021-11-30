package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactory


data class BenefitPackageDataUiModel(
    val benefitPackageData: List<BenefitPackageGradeUiModel> = emptyList()
) : BaseBenefitPackageUiModel {
    override fun type(typeFactory: BenefitPackageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class BenefitPackageGradeUiModel(
    val gradeName: String = "",
    val iconBenefitUrl: String = "",
    val descBenefit: String = "",
    val backgroundUrl: String = "",
    val isDowngrade: Boolean = false,
    val benefitItemList: List<BenefitItem> = emptyList()
)

data class BenefitItem(
    val imageUrL: String = "",
    val title: String = ""
)