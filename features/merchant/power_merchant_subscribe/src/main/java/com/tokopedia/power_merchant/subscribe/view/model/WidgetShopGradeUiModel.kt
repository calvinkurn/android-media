package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class WidgetShopGradeUiModel(
        @ShopGrade val shopGrade: String = PMShopGrade.BRONZE,
        val shopScore: Int = 0,
        val threshold: Int = 0,
        val shopLevel: Int = 0,
        val gradeBadgeImgUrl: String = "",
        val gradeBackgroundUrl: String = "",
        val nextPmCalculationDate: String = "",
        val pmStatus: String = ""
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}