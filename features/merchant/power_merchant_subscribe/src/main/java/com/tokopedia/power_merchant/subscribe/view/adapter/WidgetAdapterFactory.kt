package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

interface WidgetAdapterFactory {

    fun type(model: WidgetRegistrationHeaderUiModel): Int

    fun type(model: WidgetPotentialUiModel): Int

    fun type(model: WidgetGradeBenefitUiModel): Int

    fun type(model: WidgetDividerUiModel): Int
}