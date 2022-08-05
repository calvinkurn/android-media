package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class WidgetShopGradeUiModel(
        val isNewSeller: Boolean = true,
        val is30FirstMonday: Boolean = false,
        val pmTierType: Int = 0,
        val shopScore: Int = 0,
        val shopScoreThreshold: Int = 0,
        val pmProShopScoreThreshold: Int = 0,
        val itemSoldThreshold: Long,
        val netItemValueThreshold: Long,
        val nextMonthlyRefreshDate: String,
        val shopAge: Long = 0L,
        val gradeBackgroundUrl: String = "",
        val pmStatus: String = "",
        val shopGrade: String = ""
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}