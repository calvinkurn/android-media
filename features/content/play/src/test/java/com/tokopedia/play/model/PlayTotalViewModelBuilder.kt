package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayTotalViewUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayTotalViewModelBuilder {

    fun buildIncompleteData() = PlayTotalViewUiModel.Incomplete

    fun buildCompleteData(
            totalView: String = "1.2k"
    ) = PlayTotalViewUiModel.Complete(totalView = totalView)
}