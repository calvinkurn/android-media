package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.*
import com.tokopedia.power_merchant.subscribe.view.model.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class WidgetAdapterFactoryImpl : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(model: WidgetRegistrationHeaderUiModel): Int = RegistrationHeaderWidget.RES_LAYOUT

    override fun type(model: WidgetPotentialUiModel): Int = PotentialWidget.RES_LAYOUT

    override fun type(model: WidgetGradeBenefitUiModel): Int = GradeBenefitWidget.RES_LAYOUT

    override fun type(model: WidgetDividerUiModel): Int = DividerWidget.RES_LAYOUT

    override fun type(model: WidgetShopGradeUiModel): Int = ShopGradeWidget.RES_LAYOUT

    override fun type(model: WidgetQuitSubmissionUiModel): Int = QuitSubmissionWidget.RES_LAYOUT

    override fun type(model: WidgetExpandableUiModel): Int = ExpandableWidget.RES_LAYOUT

    override fun type(model: WidgetSingleCtaUiModel): Int = SingleCtaWidget.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RegistrationHeaderWidget.RES_LAYOUT -> RegistrationHeaderWidget(parent)
            QuitSubmissionWidget.RES_LAYOUT -> QuitSubmissionWidget(parent)
            GradeBenefitWidget.RES_LAYOUT -> GradeBenefitWidget(parent)
            ExpandableWidget.RES_LAYOUT -> ExpandableWidget(parent)
            ShopGradeWidget.RES_LAYOUT -> ShopGradeWidget(parent)
            PotentialWidget.RES_LAYOUT -> PotentialWidget(parent)
            SingleCtaWidget.RES_LAYOUT -> SingleCtaWidget(parent)
            DividerWidget.RES_LAYOUT -> DividerWidget(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}