package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class WidgetShopGradeUiModel(
        val isNewSeller: Boolean = true,
        val pmTierType: Int = 0,
        val shopScore: Int = 0,
        val threshold: Int = 0,
        val gradeBadgeImgUrl: String = "",
        val gradeBackgroundUrl: String = "",
        val pmStatus: String = ""
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}