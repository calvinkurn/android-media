package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 17/03/21
 */

data class WidgetNextShopGradeUiModel(
        val shopLevel: Int = 0,
        val shopScoreMin: Int = 0,
        @ShopGrade val gradeName: String = PMShopGrade.ADVANCED,
        val gradeBadgeUrl: String = "",
        val benefitList: List<String>
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}