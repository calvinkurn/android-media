package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class WidgetShopGradeUiModel(
        val shopScore: Int = 0,
        val threshold: Int = 0,
        val shopLevel: Int = 0,
        val shopGrade: String = "",
        val shopGradeBgUrl: String = "",
        val nextPmCalculationDate: String = "",
        val isPmActive: Boolean = false
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}