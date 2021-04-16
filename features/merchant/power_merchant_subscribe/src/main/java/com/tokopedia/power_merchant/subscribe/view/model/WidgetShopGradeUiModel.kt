package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class WidgetShopGradeUiModel(
        val periodType: String = PeriodType.TRANSITION_PERIOD,
        val isNewSeller: Boolean = true,
        @ShopGrade val shopGrade: String = PMShopGrade.BRONZE,
        val shopScore: Int = 0,
        val shopAge: Int = 0,
        val threshold: Int = 0,
        val gradeBadgeImgUrl: String = "",
        val gradeBackgroundUrl: String = "",
        val nextPmCalculationDate: String = "",
        val newSellerTenure: String = "",
        val pmStatus: String = ""
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}