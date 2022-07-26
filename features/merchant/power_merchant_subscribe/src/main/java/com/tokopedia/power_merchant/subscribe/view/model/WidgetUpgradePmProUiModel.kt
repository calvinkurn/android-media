package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 10/05/21
 */

class WidgetUpgradePmProUiModel(
    val shopInfo: PMShopInfoUiModel,
    val registrationTerms: List<PmActiveTermUiModel> = emptyList(),
    val generalBenefits: List<PMProBenefitUiModel> = emptyList(),
    val shopGrade: String = "",
    val nextMonthlyRefreshDate: String = "",
    var impressHolder: ImpressHolder = ImpressHolder()
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}