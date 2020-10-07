package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.PlayWidgetMediumOverlayUiModel


/**
 * Created by mzennis on 07/10/20.
 */
interface PlayWidgetMediumAnalytic {

    fun clickOverlayItem(data: PlayWidgetMediumOverlayUiModel)
    fun impressionOverlayItem(data: PlayWidgetMediumOverlayUiModel)

    fun clickChannelItem(data: PlayWidgetMediumChannelUiModel, position: Int)
    fun impressionChannelItem(data: PlayWidgetMediumChannelUiModel, position: Int)

    fun clickBannerItem(data: PlayWidgetMediumBannerUiModel, position: Int)
    fun impressionBannerItem(data: PlayWidgetMediumBannerUiModel, position: Int)

}