package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

interface WidgetAdapterFactory {

    fun type(model: WidgetRegistrationHeaderUiModel): Int

    fun type(model: WidgetPotentialUiModel): Int

    fun type(model: WidgetGradeBenefitUiModel): Int

    fun type(model: WidgetDividerUiModel): Int

    fun type(model: WidgetShopGradeUiModel): Int

    fun type(model: WidgetCancelDeactivationSubmissionUiModel): Int

    fun type(model: WidgetExpandableUiModel): Int

    fun type(model: WidgetSingleCtaUiModel): Int

    fun type(model: WidgetNextShopGradeUiModel): Int

    fun type(model: WidgetNextUpdateUiModel): Int

    fun type(model: WidgetPMDeactivateUiModel): Int

    fun type(model: WidgetLoadingStateUiModel): Int

    fun type(model: WidgetErrorStateUiModel): Int

    fun type(model: WidgetTickerUiModel): Int
}