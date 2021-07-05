package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidgetItemVideo
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import javax.inject.Inject

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetVideoMapper @Inject constructor() {

    fun mapWidgetItemVideo(item: PlayWidgetItemVideo): PlayWidgetVideoUiModel = PlayWidgetVideoUiModel(
            id = item.id,
            coverUrl = item.coverUrl,
            isLive = item.isLive,
            videoUrl = item.streamSource
    )
}