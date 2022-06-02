package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactory

data class BenefitPackageHeaderUiModel(
    val periodDate: String = String.EMPTY,
    val gradeName: String = String.EMPTY,
    val nextUpdate: String = String.EMPTY,
    val totalOrder: Long = Int.ZERO.toLong(),
    val netIncome: Long = Int.ZERO.toLong(),
    val shopScore: Int = Int.ZERO,
    val shopLevel: Int = Int.ZERO
): BaseBenefitPackageUiModel {

    override fun type(typeFactory: BenefitPackageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}