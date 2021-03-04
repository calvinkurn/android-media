package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
interface PlayWidgetMapper {

    fun mapWidget(data: PlayWidget, prevModel: PlayWidgetUiModel? = null): PlayWidgetUiModel
}