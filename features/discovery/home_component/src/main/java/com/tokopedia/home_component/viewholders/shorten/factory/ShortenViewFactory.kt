package com.tokopedia.home_component.viewholders.shorten.factory

import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

interface ShortenViewFactory {

    fun type(model: DealsWidgetUiModel): Int
    fun type(model: MissionWidgetUiModel): Int
}
