package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayCartInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayCartInfoModelBuilder {

    fun build(
            shouldShow: Boolean = false,
            itemCount: Int = 7
    ) = PlayCartInfoUiModel(
            shouldShow = shouldShow,
            itemCount = itemCount
    )
}