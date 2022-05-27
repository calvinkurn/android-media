package com.tokopedia.gm.common.data.source.local.model

import android.os.Parcelable
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

/**
 * Created By @ilhamsuaib on 10/03/21
 */

sealed class PMGradeWithBenefitsUiModel(
    open val gradeName: String = String.EMPTY,
    open val isTabActive: Boolean = false,
    open val tabLabel: String = String.EMPTY,
    open val tabResIcon: Int = Int.ZERO,
    open val benefitList: List<PMBenefitItemUiModel> = emptyList()
) : Parcelable {

    abstract fun getShopLevel(): Int

    @Parcelize
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

    @Parcelize
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

    @Parcelize
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

    @Parcelize
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