package com.tokopedia.home_component.viewholders.shorten.factory

import com.tokopedia.home_component.visitable.shorten.DealsUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

interface ShortenViewFactory {

    fun type(model: DealsUiModel): Int
    fun type(model: MissionWidgetUiModel): Int
}
