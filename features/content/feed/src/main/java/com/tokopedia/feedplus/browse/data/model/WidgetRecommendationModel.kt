package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface WidgetRecommendationModel {

    data class Banners(
        val banners: List<BannerWidgetModel>,
    ) : WidgetRecommendationModel

    data class Channels(
        val channels: List<PlayWidgetChannelUiModel>,
    )
}
