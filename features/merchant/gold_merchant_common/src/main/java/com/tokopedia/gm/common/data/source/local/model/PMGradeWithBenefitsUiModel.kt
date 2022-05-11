package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

sealed class PMGradeWithBenefitsUiModel(
    open val gradeName: String = "",
    open val isTabActive: Boolean = false,
    open val tabLabel: String = "",
    open val tabResIcon: Int = 0,
    open val benefitList: List<PMBenefitItemUiModel> = emptyList()
) {
    data class PM(
        override val gradeName: String = "",
        override val isTabActive: Boolean = false,
        override val tabLabel: String = "",
        override val tabResIcon: Int = 0,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(gradeName, isTabActive, tabLabel, tabResIcon, benefitList)

    data class PMProAdvance(
        override val gradeName: String = "",
        override val isTabActive: Boolean = false,
        override val tabLabel: String = "",
        override val tabResIcon: Int = 0,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(gradeName, isTabActive, tabLabel, tabResIcon, benefitList)

    data class PMProExpert(
        override val gradeName: String = "",
        override val isTabActive: Boolean = false,
        override val tabLabel: String = "",
        override val tabResIcon: Int = 0,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(gradeName, isTabActive, tabLabel, tabResIcon, benefitList)

    data class PMProUltimate(
        override val gradeName: String = "",
        override val isTabActive: Boolean = false,
        override val tabLabel: String = "",
        override val tabResIcon: Int = 0,
        override val benefitList: List<PMBenefitItemUiModel> = emptyList()
    ) : PMGradeWithBenefitsUiModel(gradeName, isTabActive, tabLabel, tabResIcon, benefitList)
}