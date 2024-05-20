package com.tokopedia.home_component.viewholders.shorten.factory

import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel

interface ShortenViewFactory {

    fun type(model: MissionWidgetUiModel): Int
    fun type(model: ThumbnailWidgetUiModel): Int
    fun type(model: ProductWidgetUiModel): Int
}
