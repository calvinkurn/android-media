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

    fun type(model: WidgetQuitSubmissionUiModel): Int

    fun type(model: WidgetExpandableUiModel): Int

    fun type(model: WidgetSingleCtaUiModel): Int
}