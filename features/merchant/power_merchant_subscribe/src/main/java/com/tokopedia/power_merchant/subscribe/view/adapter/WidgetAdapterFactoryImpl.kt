package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.GradeBenefitWidget
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PotentialWidget
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.RegistrationHeaderWidget
import com.tokopedia.power_merchant.subscribe.view.model.GradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationHeaderUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class WidgetAdapterFactoryImpl : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(model: RegistrationHeaderUiModel): Int = RegistrationHeaderWidget.RES_LAYOUT

    override fun type(model: PotentialUiModel): Int = PotentialWidget.RES_LAYOUT

    override fun type(model: GradeBenefitUiModel): Int = GradeBenefitWidget.RES_LAYOUT

}