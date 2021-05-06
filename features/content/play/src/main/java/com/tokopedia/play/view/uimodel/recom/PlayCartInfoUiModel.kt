package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
sealed class PlayCartInfoUiModel {

    abstract val shouldShow: Boolean

    data class Incomplete(override val shouldShow: Boolean) : PlayCartInfoUiModel()
    data class Complete(override val shouldShow: Boolean, val count: Int) : PlayCartInfoUiModel()
}

val PlayCartInfoUiModel.count: Int
    get() = when (this) {
        is PlayCartInfoUiModel.Incomplete -> -1
        is PlayCartInfoUiModel.Complete -> count
    }