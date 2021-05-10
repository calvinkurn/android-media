package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 10/05/21
 */

class WidgetUpgradePmProUiModel(
        val shopInfo: PMShopInfoUiModel,
        val registrationTerms: List<RegistrationTermUiModel> = emptyList(),
        val generalBenefits: List<PMGradeBenefitUiModel> = emptyList()
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}