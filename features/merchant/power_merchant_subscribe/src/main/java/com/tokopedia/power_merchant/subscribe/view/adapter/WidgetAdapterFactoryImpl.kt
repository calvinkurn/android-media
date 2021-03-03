package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.DividerWidget
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.GradeBenefitWidget
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PotentialWidget
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.RegistrationHeaderWidget
import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class WidgetAdapterFactoryImpl : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(model: WidgetRegistrationHeaderUiModel): Int = RegistrationHeaderWidget.RES_LAYOUT

    override fun type(model: WidgetPotentialUiModel): Int = PotentialWidget.RES_LAYOUT

    override fun type(model: WidgetGradeBenefitUiModel): Int = GradeBenefitWidget.RES_LAYOUT

    override fun type(model: WidgetDividerUiModel): Int = DividerWidget.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RegistrationHeaderWidget.RES_LAYOUT -> RegistrationHeaderWidget(parent)
            GradeBenefitWidget.RES_LAYOUT -> GradeBenefitWidget(parent)
            PotentialWidget.RES_LAYOUT -> PotentialWidget(parent)
            DividerWidget.RES_LAYOUT -> DividerWidget(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}