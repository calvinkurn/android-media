package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.GradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationHeaderUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

interface WidgetAdapterFactory {

    fun type(model: RegistrationHeaderUiModel): Int

    fun type(model: PotentialUiModel): Int

    fun type(model: GradeBenefitUiModel): Int
}