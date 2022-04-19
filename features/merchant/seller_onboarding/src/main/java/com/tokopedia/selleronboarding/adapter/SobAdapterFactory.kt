package com.tokopedia.selleronboarding.adapter

import com.tokopedia.selleronboarding.model.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

interface SobAdapterFactory {

    fun type(model: SobSliderHomeUiModel): Int

    fun type(model: SobSliderMessageUiModel): Int

    fun type(model: SobSliderManageUiModel): Int

    fun type(model: SobSliderPromoUiModel): Int

    fun type(model: SobSliderStatisticsUiModel): Int
}
