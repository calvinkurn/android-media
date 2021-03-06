package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 29/01/21
 */
sealed class PlayTotalViewUiModel {

    object Incomplete : PlayTotalViewUiModel()
    data class Complete(val totalView: String) : PlayTotalViewUiModel()
}

val PlayTotalViewUiModel.totalViewFmt: String
    get() = if (this is PlayTotalViewUiModel.Complete) this.totalView else "0"