package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.*
import com.tokopedia.power_merchant.subscribe.view.model.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class WidgetAdapterFactoryImpl(
        private val widgetListener: PMWidgetListener,
        private val powerMerchantTracking: PowerMerchantTracking
) : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(model: WidgetRegistrationHeaderUiModel): Int = RegistrationHeaderWidget.RES_LAYOUT

    override fun type(model: WidgetPotentialUiModel): Int = PotentialWidget.RES_LAYOUT

    override fun type(model: WidgetGradeBenefitUiModel): Int = GradeBenefitWidget.RES_LAYOUT

    override fun type(model: WidgetDividerUiModel): Int = DividerWidget.RES_LAYOUT

    override fun type(model: WidgetShopGradeUiModel): Int = ShopGradeWidget.RES_LAYOUT

    override fun type(model: WidgetCancelDeactivationSubmissionUiModel): Int = CancelDeactivationSubmissionWidget.RES_LAYOUT

    override fun type(model: WidgetExpandableUiModel): Int = ExpandableWidget.RES_LAYOUT

    override fun type(model: WidgetSingleCtaUiModel): Int = SingleCtaWidget.RES_LAYOUT

    override fun type(model: WidgetNextShopGradeUiModel): Int = NextShopGradeWidget.RES_LAYOUT

    override fun type(model: WidgetNextUpdateUiModel): Int = NextUpdateInfoWidget.RES_LAYOUT

    override fun type(model: WidgetPMDeactivateUiModel): Int = PMDeactivateWidget.RES_LAYOUT

    override fun type(model: WidgetTickerUiModel): Int = TickerWidget.RES_LAYOUT

    override fun type(model: WidgetUpgradePmProUiModel): Int = UpgradePmProWidget.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RegistrationHeaderWidget.RES_LAYOUT -> RegistrationHeaderWidget(parent, widgetListener, powerMerchantTracking)
            CancelDeactivationSubmissionWidget.RES_LAYOUT -> CancelDeactivationSubmissionWidget(parent, widgetListener)
            NextUpdateInfoWidget.RES_LAYOUT -> NextUpdateInfoWidget(parent)
            NextShopGradeWidget.RES_LAYOUT -> NextShopGradeWidget(parent)
            GradeBenefitWidget.RES_LAYOUT -> GradeBenefitWidget(parent)
            PMDeactivateWidget.RES_LAYOUT -> PMDeactivateWidget(parent, widgetListener)
            UpgradePmProWidget.RES_LAYOUT -> UpgradePmProWidget(parent, widgetListener)
            ExpandableWidget.RES_LAYOUT -> ExpandableWidget(parent, widgetListener, powerMerchantTracking)
            ShopGradeWidget.RES_LAYOUT -> ShopGradeWidget(parent, powerMerchantTracking)
            PotentialWidget.RES_LAYOUT -> PotentialWidget(parent)
            SingleCtaWidget.RES_LAYOUT -> SingleCtaWidget(parent)
            DividerWidget.RES_LAYOUT -> DividerWidget(parent)
            TickerWidget.RES_LAYOUT -> TickerWidget(parent, widgetListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}