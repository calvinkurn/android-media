package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMCurrentGradeUiModel
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class WidgetExpandableUiModel(
        val grade: PMCurrentGradeUiModel?,
        val nextMonthlyRefreshDate: String,
        val nextQuarterlyCalibrationRefreshDate: String,
        val pmStatus: String,
        val pmTierType: Int,
        val isAutoExtendEnabled: Boolean,
        val items: List<BaseExpandableItemUiModel>
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isDowngradePeriod(): Boolean = nextMonthlyRefreshDate == nextQuarterlyCalibrationRefreshDate

    fun isPmPro(): Boolean = pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
}