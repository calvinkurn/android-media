package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created By @ilhamsuaib on 10/03/21
 */

sealed class PMGradeWithBenefitsUiModel(
    open val gradeName: String = String.EMPTY,
    open val isTabActive: Boolean = false,
    open val tabLabel: String = String.EMPTY,
    open val tabResIcon: Int = Int.ZERO,
    open val benefitList: List<PMBenefitItemUiModel> = emptyList()
) {

    abstract fun getShopLevel(): Int

    data class PM(
        override val gradeName: String = String.EMPTY,
        override val isTabActive: Boolean = false,
        override val tabLabel: String = String.EMPTY,
        override val tabResIcon: Int = Int.ZERO,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(
        gradeName, isTabActive, tabLabel, tabResIcon, benefitList
    ) {
        override fun getShopLevel(): Int = PMConstant.ShopLevel.ONE
    }

    data class PMProAdvance(
        override val gradeName: String = String.EMPTY,
        override val isTabActive: Boolean = false,
        override val tabLabel: String = String.EMPTY,
        override val tabResIcon: Int = Int.ZERO,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(
        gradeName, isTabActive, tabLabel, tabResIcon, benefitList
    ) {
        override fun getShopLevel(): Int = PMConstant.ShopLevel.TWO
    }

    data class PMProExpert(
        override val gradeName: String = String.EMPTY,
        override val isTabActive: Boolean = false,
        override val tabLabel: String = String.EMPTY,
        override val tabResIcon: Int = Int.ZERO,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(
        gradeName, isTabActive, tabLabel, tabResIcon, benefitList
    ) {
        override fun getShopLevel(): Int = PMConstant.ShopLevel.THREE
    }

    data class PMProUltimate(
        override val gradeName: String = String.EMPTY,
        override val isTabActive: Boolean = false,
        override val tabLabel: String = String.EMPTY,
        override val tabResIcon: Int = Int.ZERO,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(
        gradeName, isTabActive, tabLabel, tabResIcon, benefitList
    ) {
        override fun getShopLevel(): Int = PMConstant.ShopLevel.FOUR
    }
}