package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayShareInfoModelBuilder {

    fun build(
            content: String = "Ayo nonton sekarang",
            shouldShow: Boolean = true
    ) = PlayShareInfoUiModel(
            content = content,
            shouldShow = shouldShow
    )
}