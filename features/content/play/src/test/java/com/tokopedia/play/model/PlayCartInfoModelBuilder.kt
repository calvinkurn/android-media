package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayCartInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayCartInfoModelBuilder {

    fun buildIncompleteData(
            shouldShow: Boolean = false
    ) = PlayCartInfoUiModel.Incomplete(shouldShow = shouldShow)

    fun buildCompleteData(
            shouldShow: Boolean = false,
            count: Int = 7
    ) = PlayCartInfoUiModel.Complete(
            shouldShow = shouldShow,
            count = count
    )
}