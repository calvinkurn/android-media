package com.tokopedia.home_component.viewholders.shorten.factory

import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

interface ShortenViewFactory {

    fun type(model: MissionWidgetUiModel): Int
    fun type(model: ThumbnailWidgetUiModel): Int
}
