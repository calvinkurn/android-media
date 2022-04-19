package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 02/03/21
 */

data class WidgetRegistrationHeaderUiModel(
        val shopInfo: PMShopInfoUiModel,
        val registrationTerms: List<RegistrationTermUiModel> = emptyList(),
        var selectedPmType: Int = PMConstant.PMTierType.POWER_MERCHANT
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}